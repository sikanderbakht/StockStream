package com.sikander.stockstream

import com.sikander.stockstream.domain.model.ConnectionStatus
import com.sikander.stockstream.domain.model.StockQuote
import com.sikander.stockstream.domain.repository.PriceFeedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakePriceFeedRepository : PriceFeedRepository {

    private val _connection = MutableStateFlow<ConnectionStatus>(ConnectionStatus.Disconnected)
    override val connectionStatus: StateFlow<ConnectionStatus> = _connection

    private val _quotes = MutableStateFlow<Map<String, StockQuote>>(emptyMap())
    override val quotes: StateFlow<Map<String, StockQuote>> = _quotes

    var startCalls = 0
    var stopCalls = 0

    override fun start() { startCalls++ }
    override fun stop() { stopCalls++ }

    fun setConnection(value: ConnectionStatus) {
        _connection.value = value
    }

    fun setQuotes(value: Map<String, StockQuote>) {
        _quotes.value = value
    }
}