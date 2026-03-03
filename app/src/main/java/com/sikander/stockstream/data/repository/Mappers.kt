package com.sikander.stockstream.data.repository

import com.sikander.stockstream.data.websocket.WebSocketConnectionState
import com.sikander.stockstream.domain.model.ConnectionStatus
import com.sikander.stockstream.domain.model.StockQuote
import com.sikander.stockstream.domain.model.StockSymbol

internal fun WebSocketConnectionState.toDomain(): ConnectionStatus = when (this) {
    is WebSocketConnectionState.Disconnected -> ConnectionStatus.Disconnected
    is WebSocketConnectionState.Connecting -> ConnectionStatus.Connecting
    is WebSocketConnectionState.Connected -> ConnectionStatus.Connected
    is WebSocketConnectionState.Failed -> ConnectionStatus.Failed(throwable.message ?: "Unknown error")
}

internal fun QuoteSnapshot.toDomain(): StockQuote =
    StockQuote(
        symbol = StockSymbol(symbol),
        price = price,
        previousPrice = previousPrice,
        timestamp = timestamp,
    )