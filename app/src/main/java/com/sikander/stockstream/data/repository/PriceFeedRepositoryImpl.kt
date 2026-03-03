package com.sikander.stockstream.data.repository

import com.sikander.stockstream.data.websocket.StocksWebSocketClient
import com.sikander.stockstream.data.websocket.WebSocketPriceMessage
import com.sikander.stockstream.data.websocket.toJson
import com.sikander.stockstream.data.websocket.websocketPriceMessageFromJson
import com.sikander.stockstream.domain.model.ConnectionStatus
import com.sikander.stockstream.domain.model.StockQuote
import com.sikander.stockstream.domain.repository.PriceFeedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PriceFeedRepositoryImpl(
    private val stocksWebSocketClient: StocksWebSocketClient,
    private val symbols: List<String>,
    private val externalScope: CoroutineScope
) : PriceFeedRepository {

    override val connectionStatus: StateFlow<ConnectionStatus> =
        stocksWebSocketClient.connectionState
            .map { it.toDomain() }
            .stateIn(
                scope = externalScope,
                started = SharingStarted.Eagerly,
                initialValue = ConnectionStatus.Disconnected
            )

    private val _quotesData = MutableStateFlow<Map<String, QuoteSnapshot>>(emptyMap())

    override val quotes: StateFlow<Map<String, StockQuote>> =
        _quotesData
            .map { map -> map.mapValues { (_, v) -> v.toDomain() } }
            .stateIn(
                scope = externalScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyMap()
            )

    private var feedJob: Job? = null
    private var incomingJob: Job? = null

    override fun start() {
        if (feedJob?.isActive == true) return

        stocksWebSocketClient.connect()

        if (incomingJob?.isActive != true) {
            incomingJob = externalScope.launch {
                stocksWebSocketClient.observeMessages()
                    .mapNotNull { raw -> runCatching { websocketPriceMessageFromJson(raw) }.getOrNull() }
                    .collect { msg -> applyUpdate(msg) }
            }
        }

        feedJob = externalScope.launch {
            while (isActive) {
                val now = System.currentTimeMillis()
                for (symbol in symbols) {
                    val price = nextPrice(symbol)
                    stocksWebSocketClient.send(
                        WebSocketPriceMessage(symbol = symbol, price = price, timestamp = now).toJson()
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

        stocksWebSocketClient.disconnect()
    }

    private fun applyUpdate(msg: WebSocketPriceMessage) {
        _quotesData.update { old ->
            val prev = old[msg.symbol]
            val previousPrice = prev?.price ?: msg.price
            old + (msg.symbol to QuoteSnapshot(
                symbol = msg.symbol,
                price = msg.price,
                previousPrice = previousPrice,
                timestamp = msg.timestamp
            ))
        }
    }

    private fun nextPrice(symbol: String): Double {
        val current = _quotesData.value[symbol]?.price ?: seedPrice(symbol)
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