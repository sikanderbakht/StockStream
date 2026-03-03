package com.sikander.stockstream.presentation.stocks.details

data class DetailsUiState(
    val symbol: String = "",
    val price: Double = 0.0,
    val previousPrice: Double = 0.0,
    val timestamp: Long = 0L
) {
    val isUp: Boolean get() = price > previousPrice
    val isDown: Boolean get() = price < previousPrice
}