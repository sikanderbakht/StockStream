package com.sikander.stockstream.data.websocket

import kotlinx.serialization.json.Json

internal val WebSocketJson: Json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    explicitNulls = false
    isLenient = true
}