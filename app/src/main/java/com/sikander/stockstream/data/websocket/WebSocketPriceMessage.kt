package com.sikander.stockstream.data.websocket

import kotlinx.serialization.Serializable

@Serializable
data class WebSocketPriceMessage(
    val symbol: String,
    val price: Double,
    val timestamp: Long
)