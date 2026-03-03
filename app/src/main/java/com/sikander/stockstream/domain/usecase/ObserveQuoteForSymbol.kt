package com.sikander.stockstream.domain.usecase

import com.sikander.stockstream.domain.model.StockQuote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveQuoteForSymbol(
    private val observeQuotes: ObserveQuotes
) {
    operator fun invoke(symbol: String): Flow<StockQuote?> =
        observeQuotes().map { it[symbol] }
}