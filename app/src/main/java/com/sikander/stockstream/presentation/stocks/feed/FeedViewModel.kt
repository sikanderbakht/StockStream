package com.sikander.stockstream.presentation.stocks.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sikander.stockstream.domain.model.ConnectionStatus
import com.sikander.stockstream.domain.usecase.PriceFeedUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val useCases: PriceFeedUseCases
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _isRunning = MutableStateFlow(false)

    private val isConnectedFlow: Flow<Boolean> =
        useCases.observeConnection()
            .map { it is ConnectionStatus.Connected }

    private val rowsFlow: Flow<List<StockRowUi>> =
        useCases.observeQuotes()
            .map { map ->
                map.values.map { quotes ->
                    StockRowUi(
                        symbol = quotes.symbol.value,
                        price = quotes.price,
                        previousPrice = quotes.previousPrice,
                        timestamp = quotes.timestamp
                    )
                }
            }

    val uiState: StateFlow<FeedUiState> =
        combine(
            _isRunning,
            isConnectedFlow,
            _query,
            rowsFlow
        ) { isRunning, isConnected, query, rows ->

            val filtered = rows
                .asSequence()
                .filter { it.symbol.contains(query.trim(), ignoreCase = true) }
                .sortedByDescending { it.price }
                .toList()

            FeedUiState(
                isRunning = isRunning,
                isConnected = isConnected,
                query = query,
                items = filtered
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FeedUiState()
        )

    fun onQueryChange(value: String) {
        _query.value = value
    }

    fun toggleFeed() {
        viewModelScope.launch {
            val next = !_isRunning.value
            _isRunning.value = next
            if (next) useCases.start() else useCases.stop()
        }
    }
}