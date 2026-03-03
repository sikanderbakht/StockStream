package com.sikander.stockstream.presentation.stocks.feed

import app.cash.turbine.test
import com.sikander.stockstream.FakePriceFeedRepository
import com.sikander.stockstream.MainDispatcherRule
import com.sikander.stockstream.domain.model.ConnectionStatus
import com.sikander.stockstream.domain.model.StockQuote
import com.sikander.stockstream.domain.model.StockSymbol
import com.sikander.stockstream.domain.usecase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `filters by query and sorts by price desc`() = runTest {
        val repo = FakePriceFeedRepository().apply {
            setConnection(ConnectionStatus.Connected)
            setQuotes(
                mapOf(
                    "NVDA" to StockQuote(StockSymbol("NVDA"), 300.0, 290.0, 1L),
                    "AAPL" to StockQuote(StockSymbol("AAPL"), 200.0, 210.0, 1L),
                    "GOOG" to StockQuote(StockSymbol("GOOG"), 250.0, 240.0, 1L),
                )
            )
        }

        val observeQuotes = ObserveQuotes(repo)
        val useCases = PriceFeedUseCases(
            start = StartPriceFeed(repo),
            stop = StopPriceFeed(repo),
            observeConnection = ObserveConnectionStatus(repo),
            observeQuotes = observeQuotes,
            observeQuoteForSymbol = ObserveQuoteForSymbol(observeQuotes)
        )

        val vm = FeedViewModel(useCases)

        vm.uiState.test {
            awaitItem()

            vm.onQueryChange("g")
            val s1 = awaitItem()
            assertEquals(listOf("GOOG"), s1.items.map { it.symbol })

            vm.onQueryChange("")
            val s2 = awaitItem()
            assertEquals(listOf("NVDA", "GOOG", "AAPL"), s2.items.map { it.symbol })

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleFeed calls start then stop`() = runTest {
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

        val vm = FeedViewModel(useCases)

        vm.toggleFeed()
        assertEquals(1, repo.startCalls)
        assertEquals(0, repo.stopCalls)

        vm.toggleFeed()
        assertEquals(1, repo.startCalls)
        assertEquals(1, repo.stopCalls)
    }
}