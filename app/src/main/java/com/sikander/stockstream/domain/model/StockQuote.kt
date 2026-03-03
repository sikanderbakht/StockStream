package com.sikander.stockstream.domain.model

data class StockQuote(
    val symbol: StockSymbol,
    val price: Double,
    val previousPrice: Double,
    val timestamp: Long
) {
    val change: Double get() = price - previousPrice
    val isUp: Boolean get() = price > previousPrice
    val isDown: Boolean get() = price < previousPrice
}