package com.sikander.stockstream.domain.usecase

import com.sikander.stockstream.domain.model.ConnectionStatus
import com.sikander.stockstream.domain.repository.PriceFeedRepository
import kotlinx.coroutines.flow.StateFlow

class ObserveConnectionStatus(
    private val repo: PriceFeedRepository
) {
    operator fun invoke(): StateFlow<ConnectionStatus> = repo.connectionStatus
}