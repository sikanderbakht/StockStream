package com.sikander.stockstream.data.websocket

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

internal fun WebSocketPriceMessage.toJson(): String =
    WebSocketJson.encodeToString(this)

internal fun wsPriceMessageFromJson(json: String): WebSocketPriceMessage =
    WebSocketJson.decodeFromString(json)