package com.sikander.stockstream.domain.usecase

import com.sikander.stockstream.domain.repository.PriceFeedRepository

class StopPriceFeed(
    private val repo: PriceFeedRepository
) {
    operator fun invoke() = repo.stop()
}