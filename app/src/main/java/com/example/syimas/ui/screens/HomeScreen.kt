package com.example.syimas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.syimas.R
import com.example.syimas.data.Product
import com.example.syimas.data.ShopViewModel
import com.example.syimas.ui.components.AppHeader
import com.example.syimas.ui.components.EmptyState
import com.example.syimas.ui.components.PillButton
import com.example.syimas.ui.components.ProductCard
import com.example.syimas.ui.components.SaleBanner
import com.example.syimas.ui.utils.Translations

@Composable
fun HomeScreen(
    vm: ShopViewModel,
    onOpenSummary: () -> Unit,
    onOpenStockCheck: () -> Unit,
    onOpenHistory: () -> Unit
) {
    val products by vm.products.collectAsStateWithLifecycle()
    val sales    by vm.todaySales.collectAsStateWithLifecycle()
    val lang     by vm.language.collectAsStateWithLifecycle()
    val totalToday = sales.sumOf { it.moneyEarned }

    var sheetProduct by remember { mutableStateOf<Product?>(null) }

    Column(Modifier.fillMaxSize()) {

        AppHeader()

        Column(Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp)) {
            // Pass lang to SaleBanner
            SaleBanner(totalToday, lang)

            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                PillButton(text = Translations.get("daily_summary", lang), modifier = Modifier.weight(1f), onClick = onOpenSummary)
                PillButton(text = Translations.get("stock_check", lang),   modifier = Modifier.weight(1f), onClick = onOpenStockCheck)
                PillButton(text = Translations.get("history", lang),       modifier = Modifier.weight(1f), onClick = onOpenHistory)
            }

            Spacer(Modifier.height(20.dp))
            Text(Translations.get("products", lang), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
        }

        if (products.isEmpty()) {
            EmptyState(
                illustration = R.drawable.empty_home,
                message = Translations.get("products_added_here", lang),
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(products, key = { it.id }) { p ->
                    ProductCard(p, lang) { sheetProduct = p }
                }
            }
        }
    }

    sheetProduct?.let { p ->
        SaleSheet(
            product = p,
            lang = lang, // Pass lang to SaleSheet
            onDismiss = { sheetProduct = null },
            onConfirmAmount = { amount -> vm.recordSale(p, amount); sheetProduct = null },
            onConfirmMoney  = { money  -> vm.recordSaleByMoney(p, money); sheetProduct = null }
        )
    }
}