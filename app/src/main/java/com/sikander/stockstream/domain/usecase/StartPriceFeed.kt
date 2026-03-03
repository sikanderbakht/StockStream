package com.sikander.stockstream.domain.usecase

import com.sikander.stockstream.domain.repository.PriceFeedRepository

class StartPriceFeed(
    private val repo: PriceFeedRepository
) {
    operator fun invoke() = repo.start()
}