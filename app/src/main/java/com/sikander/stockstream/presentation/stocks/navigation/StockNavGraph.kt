package com.sikander.stockstream.presentation.stocks.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.sikander.stockstream.presentation.navigation.Navigator
import com.sikander.stockstream.presentation.navigation.StockRoutes
import com.sikander.stockstream.presentation.stocks.details.StockDetailsScreen
import com.sikander.stockstream.presentation.stocks.feed.StockFeedScreen

fun NavGraphBuilder.stockNavGraph(
    navigator: Navigator
) {
    composable(StockRoutes.FEED) {
        StockFeedScreen(onStockItemClick = { item -> navigator.toDetails(item) })
    }

    composable(
        route = StockRoutes.detailsPattern,
        arguments = listOf(navArgument(StockRoutes.ARG_SYMBOL) { type = NavType.StringType })
    ) { backStackEntry ->
        val symbol = backStackEntry.arguments?.getString(StockRoutes.ARG_SYMBOL).orEmpty()

        StockDetailsScreen(
            symbol = symbol,
            onBack = navigator::back
        )
    }
}