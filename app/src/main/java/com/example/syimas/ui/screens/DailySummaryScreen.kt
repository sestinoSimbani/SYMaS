package com.example.syimas.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun DailySummaryScreen(vm: ShopViewModel, onBack: () -> Unit) {
    val sales    by vm.todaySales.collectAsStateWithLifecycle()
    val products by vm.products.collectAsStateWithLifecycle()
    val lang     by vm.language.collectAsStateWithLifecycle()

    val earned    = sales.sumOf { it.moneyEarned }
    val profit    = sales.sumOf { it.profit }
    val customers = sales.size

    val perProduct = sales.groupBy { it.productName }
        .mapValues { entry -> entry.value.sumOf { it.amountBase } }

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
            Text(Translations.get("daily_summary", lang), fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        if (sales.isEmpty()) {
            EmptyState(
                illustration = R.drawable.empty_summary,
                message = Translations.get("no_summary", lang),
                modifier = Modifier.weight(1f)
            )
            return@Column
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = SimpleDateFormat("EEEE, d MMM yyyy", Locale.getDefault()).format(Date()),
                fontSize = 14.sp,
                color = TextBlack,
                modifier = Modifier.padding(bottom = 16.dp)
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
                        Text("${Translations.get("money_earned", lang)}: Mwk ${"%,.0f".format(earned)}", fontSize = 14.sp)
                    }

                    Box(
                        modifier = Modifier
                            .background(Green.copy(alpha = 0.15f), RoundedCornerShape(50))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text("${Translations.get("profit", lang)}: Mwk ${"%,.0f".format(profit)}", fontSize = 14.sp)
                    }

                    Box(
                        modifier = Modifier
                            .background(Green.copy(alpha = 0.15f), RoundedCornerShape(50))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text("${Translations.get("customers_served", lang)}: $customers", fontSize = 14.sp)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = Translations.get("sold_per_product", lang),
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
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Green, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(Translations.get("product", lang), color = White, fontSize = 14.sp, modifier = Modifier.weight(1f))
                        Text(Translations.get("amount_sold", lang), color = White, fontSize = 14.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                        Text(Translations.get("stock_remains", lang), color = White, fontSize = 14.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                    }

                    LazyColumn {
                        val productEntries = perProduct.entries.toList()
                        items(productEntries) { (productName, amountSoldBase) ->
                            val product = products.find { it.name == productName }
                            val unit = product?.unit ?: "kg"
                            val stockRemaining = product?.stockDisplay ?: 0.0
                            val amountSoldDisplay = amountSoldBase / 1000.0

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(productName, fontSize = 14.sp, modifier = Modifier.weight(1f))
                                Text("Sold ${"%.1f".format(amountSoldDisplay)}$unit", color = Green, fontSize = 14.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                Text("${"%.1f".format(stockRemaining)}$unit", color = TextBlack, fontSize = 14.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                            }

                            if (productName != productEntries.last().key) {
                                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 12.dp))
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Green)
            ) {
                Text(Translations.get("close_day", lang), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}