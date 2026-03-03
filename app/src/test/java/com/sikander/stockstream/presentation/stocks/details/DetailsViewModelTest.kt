package com.sikander.stockstream.presentation.stocks.details

import app.cash.turbine.test
import com.sikander.stockstream.FakePriceFeedRepository
import com.sikander.stockstream.MainDispatcherRule
import com.sikander.stockstream.domain.model.ConnectionStatus
import com.sikander.stockstream.domain.model.StockQuote
import com.sikander.stockstream.domain.model.StockSymbol
import com.sikander.stockstream.domain.usecase.*
import com.sikander.stockstream.presentation.navigation.StockRoutes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import androidx.lifecycle.SavedStateHandle

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `reads symbol from SavedStateHandle and reflects quote updates`() = runTest {
        val repo = FakePriceFeedRepository().apply {
            setConnection(ConnectionStatus.Connected)
        }

        val observeQuotes = ObserveQuotes(repo)
        val useCases = PriceFeedUseCases(
            start = StartPriceFeed(repo),
            stop = StopPriceFeed(repo),
            observeConnection = ObserveConnectionStatus(repo),
            observeQuotes = observeQuotes,
            observeQuoteForSymbol = ObserveQuoteForSymbol(observeQuotes)
        )

        val handle = SavedStateHandle(
            mapOf(StockRoutes.ARG_SYMBOL to "NVDA")
        )

        val vm = DetailsViewModel(
            savedStateHandle = handle,
            useCases = useCases
        )

        vm.uiState.test {
            val initial = awaitItem()
            assertEquals("NVDA", initial.symbol)
            assertEquals(0.0, initial.price, 0.0001)

            repo.setQuotes(
                mapOf(
                    "NVDA" to StockQuote(StockSymbol("NVDA"), 123.45, 120.00, timestamp = 10L)
                )
            )

            val updated = awaitItem()
            assertEquals("NVDA", updated.symbol)
            assertEquals(123.45, updated.price, 0.0001)
            assertEquals(120.00, updated.previousPrice, 0.0001)
            assertEquals(10L, updated.timestamp)

            cancelAndIgnoreRemainingEvents()
        }
    }
}