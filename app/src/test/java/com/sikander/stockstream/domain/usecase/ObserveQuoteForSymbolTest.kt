package com.sikander.stockstream.domain.usecase

import app.cash.turbine.test
import com.sikander.stockstream.domain.model.StockQuote
import com.sikander.stockstream.domain.model.StockSymbol
import com.sikander.stockstream.domain.repository.PriceFeedRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ObserveQuoteForSymbolTest {

    private class FakeRepo : PriceFeedRepository {
        override val connectionStatus =
            MutableStateFlow(com.sikander.stockstream.domain.model.ConnectionStatus.Disconnected)
        override val quotes: StateFlow<Map<String, StockQuote>> = MutableStateFlow(emptyMap())

        override fun start() {}
        override fun stop() {}
    }

    @Test
    fun `emits quote for requested symbol`() = runTest {
        val quotesFlow = MutableStateFlow<Map<String, StockQuote>>(emptyMap())
        val repo = object : PriceFeedRepository by FakeRepo() {
            override val quotes: StateFlow<Map<String, StockQuote>> = quotesFlow
        }

        val observeQuotes = ObserveQuotes(repo)
        val sut = ObserveQuoteForSymbol(observeQuotes)

        sut("NVDA").test {
            assertEquals(null, awaitItem())

            val q1 = StockQuote(StockSymbol("NVDA"), 100.0, 95.0, timestamp = 1L)
            quotesFlow.value = mapOf("NVDA" to q1)
            assertEquals(q1, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}