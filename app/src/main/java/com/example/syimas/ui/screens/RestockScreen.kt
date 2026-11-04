package com.example.syimas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.syimas.data.ShopViewModel
import com.example.syimas.ui.components.AppHeader
import com.example.syimas.ui.theme.Green
import com.example.syimas.ui.theme.TextBlack
import com.example.syimas.ui.theme.White
import com.example.syimas.ui.utils.Translations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestockScreen(vm: ShopViewModel, onNavigateHome: () -> Unit) {
    val products by vm.products.collectAsStateWithLifecycle()
    val lang     by vm.language.collectAsStateWithLifecycle()

    var selected by remember { mutableStateOf<Long?>(null) }
    var bags     by remember { mutableStateOf("1") }
    var bagSize  by remember { mutableStateOf("") }
    var total    by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val product = products.firstOrNull { it.id == selected }

    Column(Modifier.fillMaxSize()) {
        AppHeader()

        Column(Modifier.padding(horizontal = 16.dp, vertical = 20.dp)) {
            Text(Translations.get("restock", lang), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextBlack)
            Text(Translations.get("recharge_stock", lang), fontSize = 14.sp, color = TextBlack)

            Spacer(Modifier.height(24.dp))

            Text(Translations.get("pick_product", lang), fontSize = 14.sp, color = Green, modifier = Modifier.padding(bottom = 4.dp))
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = product?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    shape = RoundedCornerShape(50),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                        focusedBorderColor = Green
                    ),
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                )
                ExposedDropdownMenu(expanded, onDismissRequest = { expanded = false }) {
                    products.forEach { p ->
                        DropdownMenuItem(text = { Text(p.name) },
                            onClick = { selected = p.id; expanded = false })
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(Translations.get("number_bags", lang), fontSize = 14.sp, color = Green, modifier = Modifier.padding(bottom = 4.dp))
            OutlinedTextField(
                value = bags,
                onValueChange = { bags = it },
                placeholder = { Text("1", color = Color.LightGray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                    focusedBorderColor = Green
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Text(Translations.get("kg_per_bag", lang), fontSize = 14.sp, color = Green, modifier = Modifier.padding(bottom = 4.dp))
            OutlinedTextField(
                value = bagSize,
                onValueChange = { bagSize = it },
                placeholder = { Text("Eg. 50 Kg", color = Color.LightGray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                    focusedBorderColor = Green
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Text(Translations.get("total_price_paid", lang), fontSize = 14.sp, color = Green, modifier = Modifier.padding(bottom = 4.dp))
            OutlinedTextField(
                value = total,
                onValueChange = { total = it },
                placeholder = { Text("", color = Color.LightGray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                    focusedBorderColor = Green
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(32.dp))

            Button(
                enabled = product != null && bags.toIntOrNull() != null
                        && bagSize.toDoubleOrNull() != null && total.toDoubleOrNull() != null,
                onClick = {
                    vm.restock(product!!, bags.toInt(), bagSize.toDouble(), total.toDouble())
                    bags = "1"; bagSize = ""; total = ""; selected = null
                    showSuccessDialog = true
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Green),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(Translations.get("save_restock", lang), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = White)
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text(Translations.get("restocked_success", lang), fontWeight = FontWeight.Bold) },
            text = { Text(Translations.get("check_product_now", lang)) },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onNavigateHome()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Green),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(Translations.get("check_product", lang), color = White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSuccessDialog = false }) {
                    Text(Translations.get("cancel", lang), color = TextBlack)
                }
            }
        )
    }
}