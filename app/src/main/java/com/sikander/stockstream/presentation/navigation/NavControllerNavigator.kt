package com.sikander.stockstream.presentation.navigation

import androidx.navigation.NavHostController

class NavControllerNavigator(
    private val navController: NavHostController
) : Navigator {
    override fun toDetails(symbol: String) {
        navController.navigate(StockRoutes.details(symbol))
    }

    override fun back() {
        navController.popBackStack()
    }
}