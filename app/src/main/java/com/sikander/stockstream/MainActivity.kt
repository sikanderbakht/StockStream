package com.sikander.stockstream

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.sikander.stockstream.presentation.navigation.AppNavHost
import com.sikander.stockstream.ui.StockStreamTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StockStreamTheme {
                AppNavHost(modifier = Modifier.fillMaxSize())
            }
        }
    }
}