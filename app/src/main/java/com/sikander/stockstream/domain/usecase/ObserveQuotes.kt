package com.sikander.stockstream.domain.usecase

import com.sikander.stockstream.domain.model.StockQuote
import com.sikander.stockstream.domain.repository.PriceFeedRepository
import kotlinx.coroutines.flow.StateFlow

class ObserveQuotes(
    private val repo: PriceFeedRepository
) {
    operator fun invoke(): StateFlow<Map<String, StockQuote>> = repo.quotes
}