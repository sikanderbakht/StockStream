package com.sikander.stockstream.presentation.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun priceColors(): Triple<Color, Color, Color> {
    val upColor = Color(0xFF2E7D32)
    val downColor = Color(0xFFC62828)
    val neutralColor = Color(0xFF9E9E9E)
    return Triple(upColor, downColor, neutralColor)
}