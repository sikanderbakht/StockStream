package com.sikander.stockstream.domain.usecase

data class PriceFeedUseCases(
    val start: StartPriceFeed,
    val stop: StopPriceFeed,
    val observeConnection: ObserveConnectionStatus,
    val observeQuotes: ObserveQuotes,
    val observeQuoteForSymbol: ObserveQuoteForSymbol
)