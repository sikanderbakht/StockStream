package com.sikander.stockstream.presentation.stocks.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sikander.stockstream.domain.usecase.PriceFeedUseCases
import com.sikander.stockstream.presentation.navigation.StockRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCases: PriceFeedUseCases
) : ViewModel() {

    private val symbol: String =
        savedStateHandle.get<String>(StockRoutes.ARG_SYMBOL).orEmpty()

    val uiState: StateFlow<DetailsUiState> =
        useCases.observeQuoteForSymbol(symbol)
            .map { quote ->
                DetailsUiState(
                    symbol = symbol,
                    price = quote?.price ?: 0.0,
                    previousPrice = quote?.previousPrice ?: 0.0,
                    timestamp = quote?.timestamp ?: 0L
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DetailsUiState(symbol = symbol)
            )
}