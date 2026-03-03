package com.sikander.stockstream.di

import com.sikander.stockstream.domain.repository.PriceFeedRepository
import com.sikander.stockstream.domain.usecase.ObserveConnectionStatus
import com.sikander.stockstream.domain.usecase.ObserveQuoteForSymbol
import com.sikander.stockstream.domain.usecase.ObserveQuotes
import com.sikander.stockstream.domain.usecase.PriceFeedUseCases
import com.sikander.stockstream.domain.usecase.StartPriceFeed
import com.sikander.stockstream.domain.usecase.StopPriceFeed
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun providePriceFeedUseCases(repo: PriceFeedRepository): PriceFeedUseCases {
        val observeQuotes = ObserveQuotes(repo)
        return PriceFeedUseCases(
            start = StartPriceFeed(repo),
            stop = StopPriceFeed(repo),
            observeConnection = ObserveConnectionStatus(repo),
            observeQuotes = observeQuotes,
            observeQuoteForSymbol = ObserveQuoteForSymbol(observeQuotes)
        )
    }
}