package com.sikander.stockstream.presentation.stocks.feed

data class FeedUiState(
    val isRunning: Boolean = false,
    val isConnected: Boolean = false,
    val query: String = "",
    val items: List<StockRowUi> = emptyList()
)

data class StockRowUi(
    val symbol: String,
    val price: Double,
    val previousPrice: Double,
    val timestamp: Long
) {
    val isUp: Boolean get() = price > previousPrice
    val isDown: Boolean get() = price < previousPrice
}