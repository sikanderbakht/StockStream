package com.sikander.stockstream.data.websocket

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface StocksWebSocketClient {
    val connectionState: StateFlow<WebSocketConnectionState>
    fun observeMessages(): Flow<String>
    fun connect()
    fun disconnect()
    fun send(text: String): Boolean
}