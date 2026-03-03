package com.sikander.stockstream.data.websocket

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

internal fun WebSocketPriceMessage.toJson(): String =
    WebSocketJson.encodeToString(this)

internal fun websocketPriceMessageFromJson(json: String): WebSocketPriceMessage =
    WebSocketJson.decodeFromString(json)