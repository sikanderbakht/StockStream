package com.sikander.stockstream.di

import com.sikander.stockstream.data.repository.PriceFeedRepositoryImpl
import com.sikander.stockstream.data.websocket.StocksWebSocketClient
import com.sikander.stockstream.domain.repository.PriceFeedRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePriceFeedRepository(
        stocksWebSocketClient: StocksWebSocketClient,
        @StockSymbols symbols: List<String>,
        appScope: CoroutineScope
    ): PriceFeedRepository =
        PriceFeedRepositoryImpl(
            stocksWebSocketClient = stocksWebSocketClient,
            symbols = symbols,
            externalScope = appScope
        )
}