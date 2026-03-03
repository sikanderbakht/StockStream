package com.sikander.stockstream.data.repository

import app.cash.turbine.test
import com.sikander.stockstream.data.websocket.WebSocketPriceMessage
import com.sikander.stockstream.data.websocket.toJson
import com.sikander.stockstream.domain.model.StockQuote
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PriceFeedRepositoryImplTest {

    @Test
    fun `incoming messages update quotes and maintain previousPrice`() = runTest {
        val stocksWebSocketClient = FakeStocksWebSocketClient()
        val repo = PriceFeedRepositoryImpl(
            stocksWebSocketClient = stocksWebSocketClient,
            symbols = listOf("NVDA"),
            externalScope = backgroundScope
        )

        repo.quotes.test {
            assertEquals(emptyMap<String, StockQuote>(), awaitItem())

            repo.start()
            runCurrent()

            stocksWebSocketClient.emitIncoming(WebSocketPriceMessage("NVDA", 100.0, timestamp = 1L).toJson())
            runCurrent()

            val first = awaitItem()
            val q1 = first.getValue("NVDA")
            assertEquals(100.0, q1.price, 0.0001)
            assertEquals(100.0, q1.previousPrice, 0.0001)
            assertEquals(1L, q1.timestamp)

            stocksWebSocketClient.emitIncoming(WebSocketPriceMessage("NVDA", 105.0, timestamp = 2L).toJson())
            runCurrent()

            val second = awaitItem()
            val q2 = second.getValue("NVDA")
            assertEquals(105.0, q2.price, 0.0001)
            assertEquals(100.0, q2.previousPrice, 0.0001)
            assertEquals(2L, q2.timestamp)

            repo.stop()
            runCurrent()

            cancelAndIgnoreRemainingEvents()
        }
    }
}