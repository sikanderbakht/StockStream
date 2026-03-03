package com.sikander.stockstream.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SkeletonStockRow(
    modifier: Modifier = Modifier
) {
    val faint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f)
    val faint2 = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .fillMaxWidth(0.28f)
                        .background(faint, RoundedCornerShape(8.dp))
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .height(10.dp)
                        .fillMaxWidth(0.14f)
                        .background(faint2, RoundedCornerShape(8.dp))
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .width(88.dp)
                        .background(faint, RoundedCornerShape(8.dp))
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .height(10.dp)
                        .width(54.dp)
                        .background(faint2, RoundedCornerShape(8.dp))
                )
            }

            Spacer(Modifier.width(14.dp))

            Box(
                modifier = Modifier
                    .size(width = 16.dp, height = 16.dp)
                    .background(faint2, RoundedCornerShape(6.dp))
            )
        }
    }
}