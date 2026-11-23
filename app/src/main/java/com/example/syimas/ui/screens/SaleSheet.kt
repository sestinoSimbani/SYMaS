package com.example.syimas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.syimas.data.Product
import com.example.syimas.ui.theme.Green
import com.example.syimas.ui.theme.Red
import com.example.syimas.ui.utils.Translations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleSheet(
    product: Product,
    lang: String, // <--- ADDED LANG PARAMETER
    onDismiss: () -> Unit,
    onConfirmAmount: (Double) -> Unit,
    onConfirmMoney : (Double) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var mode by remember { mutableStateOf("amount") }
    var input by remember { mutableStateOf("") }

    val quick = remember(product.id) {
        product.quickAmounts.split(",").mapNotNull { it.trim().toDoubleOrNull() }
    }

    val inputVal = input.toDoubleOrNull() ?: 0.0
    val price = if (mode == "amount") inputVal / 1000.0 * product.sellingPrice else inputVal
    val amount = if (mode == "money")  inputVal / product.sellingPrice * 1000.0 else inputVal
    val tooMuch = amount > product.stockBase
    val lowStock = product.stockBase <= product.lowStockLevel

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(Modifier.padding(20.dp)) {
            // ---- Header ----
            Text(product.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)

            // Translated "left"
            Text("${product.stockDisplay}${product.unit} ${Translations.get("left", lang)}", color = Green, fontSize = 14.sp)

            if (lowStock) {
                Spacer(Modifier.height(4.dp))
                Text(Translations.get("low_stock", lang), color = Red, fontSize = 12.sp)
            }

            Spacer(Modifier.height(16.dp))

            // ---- Mode switch ----
            TabRow(selectedTabIndex = if (mode == "amount") 0 else 1) {
                Tab(selected = mode == "amount", onClick = { mode = "amount"; input = "" },
                    text = { Text(Translations.get("by_amount", lang)) })
                Tab(selected = mode == "money",  onClick = { mode = "money";  input = "" },
                    text = { Text(Translations.get("by_money", lang)) })
            }

            Spacer(Modifier.height(16.dp))

            // ---- Quick buttons ----
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement   = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(140.dp)
            ) {
                items(quick.size) { index ->
                    val q = quick[index]
                    OutlinedButton(onClick = { input = q.toInt().toString() }) {
                        Text(if (mode == "amount")
                            "${q.toInt()} ${if (product.type=="weight") "g" else "ml"}"
                        else "MK ${q.toInt()}")
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // ---- Custom input ----
            OutlinedTextField(
                value = input,
                onValueChange = { input = it.filter { c -> c.isDigit() || c == '.' } },
                label = { Text(if (mode == "amount") Translations.get("amount", lang) else Translations.get("money_mk", lang)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // ---- Live result ----
            if (mode == "amount") {
                Text("${Translations.get("price", lang)}:  MK ${"%.0f".format(price)}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            } else {
                Text("${Translations.get("you_will_give", lang)}:  ${"%.0f".format(amount)} ${if (product.type=="weight") "g" else "ml"}",
                    fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            if (tooMuch) {
                Text(Translations.get("not_enough_stock", lang), color = Red, fontSize = 13.sp)
            }

            Spacer(Modifier.height(20.dp))

            // ---- Confirm ----
            Button(
                onClick = {
                    if (mode == "amount") onConfirmAmount(amount) else onConfirmMoney(inputVal)
                },
                enabled = inputVal > 0 && !tooMuch,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text(Translations.get("confirm_sale", lang), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}