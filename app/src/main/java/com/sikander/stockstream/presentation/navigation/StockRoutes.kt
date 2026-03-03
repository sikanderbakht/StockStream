package com.sikander.stockstream.presentation.navigation

import android.net.Uri

object StockRoutes {
    const val FEED = "stocks/feed"
    const val DETAILS_BASE = "stocks/details"
    const val ARG_SYMBOL = "symbol"

    fun details(symbol: String): String = "$DETAILS_BASE/${Uri.encode(symbol)}"

    val detailsPattern: String = "$DETAILS_BASE/{$ARG_SYMBOL}"
}