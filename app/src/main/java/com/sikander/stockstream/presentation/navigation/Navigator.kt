package com.sikander.stockstream.presentation.navigation

interface Navigator {
    fun toDetails(stockItemTitle: String)
    fun back()
}