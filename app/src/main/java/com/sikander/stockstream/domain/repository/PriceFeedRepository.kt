package com.sikander.stockstream.domain.repository

import com.sikander.stockstream.domain.model.ConnectionStatus
import com.sikander.stockstream.domain.model.StockQuote
import kotlinx.coroutines.flow.StateFlow

interface PriceFeedRepository {
    val connectionStatus: StateFlow<ConnectionStatus>
    val quotes: StateFlow<Map<String, StockQuote>>

    fun start()
    fun stop()
}