package com.sikander.stockstream.presentation.stocks.feed

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sikander.stockstream.presentation.ui.components.AppBackground
import com.sikander.stockstream.presentation.ui.components.ConnectionChip
import com.sikander.stockstream.presentation.ui.components.GlassCard
import com.sikander.stockstream.presentation.ui.components.SkeletonStockRow
import com.sikander.stockstream.presentation.ui.theme.priceColors
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockFeedScreen(
    onStockItemClick: (String) -> Unit,
    feedViewModel: FeedViewModel = hiltViewModel()
) {
    val state by feedViewModel.uiState.collectAsStateWithLifecycle()

    val bg = AppBackground()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    title = {
                        Text(
                            text = "Stock Tracker",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    actions = {
                        ConnectionChip(isConnected = state.isConnected)
                        Spacer(Modifier.width(12.dp))
                    }
                )
            },
            bottomBar = {
                BottomAppBar(containerColor = Color.Transparent) {
                    Button(
                        onClick = feedViewModel::toggleFeed,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                        )
                    ) {
                        Text(if (state.isRunning) "Stop Feed" else "Start Feed")
                    }
                }
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    OutlinedTextField(
                        value = state.query,
                        onValueChange = feedViewModel::onQueryChange,
                        placeholder = {
                            Text(
                                text = "Search stocks...",
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(
                        bottom = 12.dp + padding.calculateBottomPadding()
                    )
                ) {

                    if (state.items.isEmpty()) {
                        item {
                            FeedInlineHint(
                                isRunning = state.isRunning,
                                query = state.query,
                                onStartClick = feedViewModel::toggleFeed,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 6.dp, bottom = 10.dp)
                            )
                        }

                        items(7) { _ ->
                            SkeletonStockRow(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    } else {
                        items(
                            items = state.items,
                            key = { it.symbol }
                        ) { row ->
                            StockCardRow(
                                row = row,
                                onClick = { onStockItemClick(row.symbol) },
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .animateItem()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StockCardRow(
    row: StockRowUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val arrow = when {
        row.isUp -> "↑"
        row.isDown -> "↓"
        else -> "—"
    }

    val change = row.price - row.previousPrice
    val changeText = when {
        change > 0 -> "+${String.format(Locale.US, "%.2f", change)}"
        change < 0 -> String.format(Locale.US, "%.2f", change)
        else -> "0.00"
    }

    val (upColor, downColor, neutralColor) = priceColors()
    val changeColor = when {
        row.isUp -> upColor
        row.isDown -> downColor
        else -> neutralColor
    }

    val titleColor = MaterialTheme.colorScheme.onSurface
    val subColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)

    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = row.symbol,
                    style = MaterialTheme.typography.titleMedium,
                    color = titleColor,
                    maxLines = 1
                )
                Text(
                    text = "—",
                    style = MaterialTheme.typography.labelSmall,
                    color = subColor,
                    maxLines = 1
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(
                    text = "$${String.format(Locale.US, "%.2f", row.price)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = titleColor,
                    maxLines = 1,
                    softWrap = false
                )
                Text(
                    text = changeText,
                    style = MaterialTheme.typography.labelSmall,
                    color = changeColor,
                    maxLines = 1,
                    softWrap = false
                )
            }

            Spacer(Modifier.width(10.dp))

            Text(
                text = arrow,
                color = changeColor,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}