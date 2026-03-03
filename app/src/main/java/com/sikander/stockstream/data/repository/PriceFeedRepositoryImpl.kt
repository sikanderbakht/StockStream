package com.sikander.stockstream.data.repository

import com.sikander.stockstream.data.websocket.StocksWebSocketClient
import com.sikander.stockstream.data.websocket.WebSocketConnectionState
import com.sikander.stockstream.data.websocket.WebSocketPriceMessage
import com.sikander.stockstream.data.websocket.toJson
import com.sikander.stockstream.data.websocket.wsPriceMessageFromJson
import com.sikander.stockstream.domain.repository.PriceFeedRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class PriceFeedRepositoryImpl(
    private val wsClient: StocksWebSocketClient,
    private val symbols: List<String>,
    private val externalScope: CoroutineScope
) : PriceFeedRepository {

    override val connectionState: StateFlow<WebSocketConnectionState> = wsClient.connectionState

    private val _quotes = MutableStateFlow<Map<String, QuoteSnapshot>>(emptyMap())
    override val quotes: StateFlow<Map<String, QuoteSnapshot>> = _quotes.asStateFlow()

    private var feedJob: Job? = null
    private var incomingJob: Job? = null

    override fun start() {
        if (feedJob?.isActive == true) return

        wsClient.connect()

        if (incomingJob?.isActive != true) {
            incomingJob = externalScope.launch {
                wsClient.observeMessages()
                    .mapNotNull { raw ->
                        runCatching { wsPriceMessageFromJson(raw) }.getOrNull()
                    }
                    .collect { msg ->
                        applyUpdate(msg)
                    }
            }
        }

        feedJob = externalScope.launch {
            while (isActive) {
                val now = System.currentTimeMillis()
                for (symbol in symbols) {
                    val price = nextPrice(symbol)
                    wsClient.send(
                        WebSocketPriceMessage(
                            symbol = symbol,
                            price = price,
                            ts = now
                        ).toJson()
                    )
                }
                delay(2_000)
            }
        }
    }

    override fun stop() {
        feedJob?.cancel()
        feedJob = null

        incomingJob?.cancel()
        incomingJob = null

        wsClient.disconnect()
    }

    private fun applyUpdate(msg: WebSocketPriceMessage) {
        _quotes.update { old ->
            val prev = old[msg.symbol]
            val previousPrice = prev?.price ?: msg.price
            old + (msg.symbol to QuoteSnapshot(
                symbol = msg.symbol,
                price = msg.price,
                previousPrice = previousPrice,
                ts = msg.ts
            ))
        }
    }

    private fun nextPrice(symbol: String): Double {
        val current = _quotes.value[symbol]?.price ?: seedPrice(symbol)
        val delta = (Math.random() - 0.5) * 4.0
        val next = (current + delta).coerceAtLeast(0.01)
        return ((next * 100.0).toInt() / 100.0)
    }

    private fun seedPrice(symbol: String): Double {
        val base = (symbol.hashCode().absoluteValue % 900) + 50
        return base.toDouble()
    }

    private val Int.absoluteValue: Int get() = if (this < 0) -this else this
}