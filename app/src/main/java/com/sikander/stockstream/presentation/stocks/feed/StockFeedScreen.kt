package com.sikander.stockstream.presentation.stocks.feed

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sikander.stockstream.presentation.ui.components.ConnectionChip
import com.sikander.stockstream.presentation.ui.components.GlassCard
import com.sikander.stockstream.presentation.ui.theme.priceColors
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockFeedScreen(
    onStockItemClick: (String) -> Unit
) {
    var isRunning by rememberSaveable { mutableStateOf(false) }
    var isConnected by rememberSaveable { mutableStateOf(true) }
    var query by rememberSaveable { mutableStateOf("") }

    val allItems = remember { mockRows() }

    val items = remember(query, allItems) {
        allItems
            .filter { it.symbol.contains(query.trim(), ignoreCase = true) }
            .sortedByDescending { it.price }
    }

    val bg = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surface
        )
    )

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
                        ConnectionChip(isConnected = isConnected)
                        Spacer(Modifier.width(12.dp))
                    }
                )
            },
            bottomBar = {
                BottomAppBar(containerColor = Color.Transparent) {
                    Button(
                        onClick = { isRunning = !isRunning },
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
                        Text(if (isRunning) "Stop Feed" else "Start Feed")
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
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 2.dp)
                ) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
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
                    contentPadding = PaddingValues(bottom = 12.dp + padding.calculateBottomPadding())
                ) {
                    items(
                        items = items,
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
            Column(
                modifier = Modifier.weight(1f)
            ) {
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

private fun mockRows(): List<StockRowUi> = listOf(
    StockRowUi("NVDA", 875.12, 862.78),
    StockRowUi("AAPL", 192.44, 193.64),
    StockRowUi("GOOG", 154.33, 153.71),
    StockRowUi("TSLA", 201.09, 204.50),
    StockRowUi("AMZN", 176.55, 175.50),
    StockRowUi("MSFT", 412.10, 408.33),
    StockRowUi("META", 480.50, 482.72),
)