package com.sikander.stockstream.data.repository

data class QuoteSnapshot(
    val symbol: String,
    val price: Double,
    val previousPrice: Double,
    val timestamp: Long
)