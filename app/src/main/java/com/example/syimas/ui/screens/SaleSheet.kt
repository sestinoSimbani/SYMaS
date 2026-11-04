package com.example.syimas.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleSheet(
    product: Product,
    onDismiss: () -> Unit,
    onConfirmAmount: (Double) -> Unit,
    onConfirmMoney : (Double) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var mode by remember { mutableStateOf("amount") }         // "amount" or "money"
    var input by remember { mutableStateOf("") }              // what the user typed

    // Quick button list from the product (comma-separated → Doubles)
    val quick = remember(product.id) {
        product.quickAmounts.split(",").mapNotNull { it.trim().toDoubleOrNull() }
    }

    // Live calculations
    val inputVal = input.toDoubleOrNull() ?: 0.0
    val price = if (mode == "amount") inputVal / 1000.0 * product.sellingPrice else inputVal
    val amount = if (mode == "money")  inputVal / product.sellingPrice * 1000.0 else inputVal
    val tooMuch = amount > product.stockBase
    val lowStock = product.stockBase <= product.lowStockLevel

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(Modifier.padding(20.dp)) {
            // ---- Header ----
            Text(product.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("${product.stockDisplay}${product.unit} left", color = Green, fontSize = 14.sp)
            if (lowStock) {
                Spacer(Modifier.height(4.dp))
                Text("Low stock – consider restocking", color = Red, fontSize = 12.sp)
            }

            Spacer(Modifier.height(16.dp))

            // ---- Mode switch ----
            TabRow(selectedTabIndex = if (mode == "amount") 0 else 1) {
                Tab(selected = mode == "amount", onClick = { mode = "amount"; input = "" },
                    text = { Text("By amount") })
                Tab(selected = mode == "money",  onClick = { mode = "money";  input = "" },
                    text = { Text("By money") })
            }

            Spacer(Modifier.height(16.dp))

            // ---- Quick buttons ----
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement   = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(140.dp)
            ) {
                items(quick) { q ->
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
                label = { Text(if (mode == "amount") "Amount" else "Money (MK)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // ---- Live result ----
            if (mode == "amount") {
                Text("Price:  MK ${"%.0f".format(price)}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            } else {
                Text("You will give:  ${"%.0f".format(amount)} ${if (product.type=="weight") "g" else "ml"}",
                    fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            if (tooMuch) {
                Text("Not enough stock!", color = Red, fontSize = 13.sp)
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
                Text("Confirm sale", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}