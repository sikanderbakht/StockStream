package com.sikander.stockstream.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.sikander.stockstream.presentation.stocks.navigation.stockNavGraph

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navigator = remember(navController) { NavControllerNavigator(navController) }

    NavHost(
        navController = navController,
        startDestination = StockRoutes.FEED,
        modifier = modifier
    ) {
        stockNavGraph(navigator)
    }
}