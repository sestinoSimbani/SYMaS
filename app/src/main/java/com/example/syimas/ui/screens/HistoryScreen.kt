package com.example.syimas.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.syimas.R
import com.example.syimas.data.ShopViewModel
import com.example.syimas.ui.components.EmptyState
import com.example.syimas.ui.theme.Green
import com.example.syimas.ui.theme.TextBlack
import com.example.syimas.ui.theme.White
import com.example.syimas.ui.utils.Translations
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(vm: ShopViewModel, onBack: () -> Unit) {
    val sales by vm.allSales.collectAsStateWithLifecycle()
    val lang  by vm.language.collectAsStateWithLifecycle()

    var showClearDialog by remember { mutableStateOf(false) }

    val groupFmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val displayFmt = SimpleDateFormat("EEEE, d MMM yyyy", Locale.getDefault())

    val grouped = sales.groupBy { groupFmt.format(Date(it.timestamp)) }
        .toSortedMap(compareByDescending { it })

    Column(Modifier.fillMaxSize()) {
        Spacer(Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(Modifier.width(4.dp))
            Text(Translations.get("history", lang), fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.weight(1f))

            if (sales.isNotEmpty()) {
                IconButton(onClick = { showClearDialog = true }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Clear History",
                        tint = Color.Gray
                    )
                }
            }
        }

        if (grouped.isEmpty()) {
            EmptyState(
                illustration = R.drawable.empty_history,
                message = Translations.get("no_history", lang),
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(grouped.entries.toList()) { (dateStr, list) ->
                    val totalSales = list.sumOf { it.moneyEarned }
                    val totalProfit = list.sumOf { it.profit }
                    val dateObj = groupFmt.parse(dateStr)
                    val displayDate = if (dateObj != null) displayFmt.format(dateObj) else dateStr

                    Column {
                        Text(
                            text = displayDate,
                            fontSize = 14.sp,
                            color = TextBlack,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = White),
                            border = BorderStroke(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(Green.copy(alpha = 0.15f), RoundedCornerShape(50))
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text("${Translations.get("total_sales", lang)}: Mwk ${"%,.0f".format(totalSales)}", color = TextBlack, fontSize = 14.sp)
                                }

                                Box(
                                    modifier = Modifier
                                        .background(Green.copy(alpha = 0.15f), RoundedCornerShape(50))
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text("${Translations.get("profit_made", lang)}: Mwk ${"%,.0f".format(totalProfit)}", color = TextBlack, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text(Translations.get("clear_history", lang)) },
            text = { Text(Translations.get("clear_history_desc", lang)) },
            confirmButton = {
                Button(
                    onClick = {
                        vm.clearHistory()
                        showClearDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(Translations.get("yes_clear", lang))
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text(Translations.get("cancel", lang))
                }
            }
        )
    }
}