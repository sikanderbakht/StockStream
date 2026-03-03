package com.sikander.stockstream.presentation.stocks.feed

data class FeedUiState(
    val isConnected: Boolean = true,
    val isRunning: Boolean = false,
    val query: String = "",
    val items: List<StockRowUi> = emptyList()
)

data class StockRowUi(
    val symbol: String,
    val price: Double,
    val previousPrice: Double,
) {
    val isUp: Boolean get() = price > previousPrice
    val isDown: Boolean get() = price < previousPrice
}