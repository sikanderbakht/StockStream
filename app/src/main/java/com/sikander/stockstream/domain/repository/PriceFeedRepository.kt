package com.sikander.stockstream.domain.repository

import com.sikander.stockstream.data.repository.QuoteSnapshot
import com.sikander.stockstream.data.websocket.WebSocketConnectionState
import kotlinx.coroutines.flow.StateFlow

interface PriceFeedRepository {
    val connectionState: StateFlow<WebSocketConnectionState>
    val quotes: StateFlow<Map<String, QuoteSnapshot>>

    fun start()
    fun stop()
}