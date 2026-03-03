package com.sikander.stockstream.di

import com.sikander.stockstream.data.websocket.OkHttpStockWebSocketClient
import com.sikander.stockstream.data.websocket.StocksWebSocketClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideStockWebSocketClient(okHttpClient: OkHttpClient): StocksWebSocketClient =
        OkHttpStockWebSocketClient(okHttpClient)
}