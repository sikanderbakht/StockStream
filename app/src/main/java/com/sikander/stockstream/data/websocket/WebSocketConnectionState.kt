package com.sikander.stockstream.data.websocket

sealed class WebSocketConnectionState {
    data object Disconnected : WebSocketConnectionState()
    data object Connecting : WebSocketConnectionState()
    data object Connected : WebSocketConnectionState()
    data class Failed(val throwable: Throwable) : WebSocketConnectionState()
}