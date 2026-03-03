package com.sikander.stockstream.data.repository

import com.sikander.stockstream.data.websocket.StocksWebSocketClient
import com.sikander.stockstream.data.websocket.WebSocketConnectionState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeStocksWebSocketClient : StocksWebSocketClient {

    private val _connectionState = MutableStateFlow<WebSocketConnectionState>(WebSocketConnectionState.Disconnected)
    override val connectionState: StateFlow<WebSocketConnectionState> = _connectionState.asStateFlow()

    private val _messages = MutableSharedFlow<String>(extraBufferCapacity = 64)
    private val messages: SharedFlow<String> = _messages.asSharedFlow()

    val sent = mutableListOf<String>()
    var connectCalls = 0
    var disconnectCalls = 0

    override fun connect() {
        connectCalls++
        _connectionState.value = WebSocketConnectionState.Connected
    }

    override fun disconnect() {
        disconnectCalls++
        _connectionState.value = WebSocketConnectionState.Disconnected
    }

    override fun observeMessages() = messages

    override fun send(text: String): Boolean {
        sent += text
        return true
    }

    suspend fun emitIncoming(text: String) {
        _messages.emit(text)
    }

    fun setConnection(state: WebSocketConnectionState) {
        _connectionState.value = state
    }
}