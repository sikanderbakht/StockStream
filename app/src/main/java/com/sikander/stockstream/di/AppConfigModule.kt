package com.sikander.stockstream.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppConfigModule {

    @Provides
    @StockSymbols
    fun provideSymbols(): List<String> = listOf(
        "AAPL","GOOG","TSLA","AMZN","MSFT","NVDA","META","NFLX","AMD","INTC",
        "UBER","LYFT","SNAP","SHOP","ORCL","IBM","BABA","TSM","QCOM","AVGO",
        "CRM","ADBE","PYPL","SQ","NIO"
    )
}