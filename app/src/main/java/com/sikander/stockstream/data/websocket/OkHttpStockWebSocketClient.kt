package com.sikander.stockstream.data.websocket

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class OkHttpStockWebSocketClient(
    private val okHttpClient: OkHttpClient,
    private val url: String = "wss://ws.postman-echo.com/raw"
) : StocksWebSocketClient {

    private var webSocket: WebSocket? = null

    private val _connectionState =
        MutableStateFlow<WebSocketConnectionState>(WebSocketConnectionState.Disconnected)
    override val connectionState: StateFlow<WebSocketConnectionState> =
        _connectionState.asStateFlow()

    private val _incoming = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 128,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override fun observeMessages(): Flow<String> = _incoming.asSharedFlow()

    override fun connect() {
        val alreadyConnected = webSocket != null && _connectionState.value is WebSocketConnectionState.Connected
        if (alreadyConnected) return

        webSocket?.cancel()
        webSocket = null

        _connectionState.value = WebSocketConnectionState.Connecting

        val request = Request.Builder().url(url).build()
        webSocket = okHttpClient.newWebSocket(request, listener)
    }

    override fun disconnect() {
        webSocket?.close(1000, "Client disconnect")
        webSocket = null
        _connectionState.value = WebSocketConnectionState.Disconnected
    }

    override fun send(text: String): Boolean = webSocket?.send(text) == true

    private val listener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            _connectionState.value = WebSocketConnectionState.Connected
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            _incoming.tryEmit(text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {

        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            this@OkHttpStockWebSocketClient.webSocket = null
            _connectionState.value = WebSocketConnectionState.Failed(t)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            this@OkHttpStockWebSocketClient.webSocket = null
            _connectionState.value = WebSocketConnectionState.Disconnected
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            _connectionState.value = WebSocketConnectionState.Disconnected
        }
    }
}