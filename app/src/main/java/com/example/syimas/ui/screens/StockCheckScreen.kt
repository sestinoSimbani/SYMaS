package com.example.syimas.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.syimas.data.Product
import com.example.syimas.data.ShopViewModel
import com.example.syimas.ui.theme.Green
import com.example.syimas.ui.theme.TextBlack
import com.example.syimas.ui.theme.White
import com.example.syimas.ui.utils.Translations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockCheckScreen(vm: ShopViewModel, onBack: () -> Unit) {
    val products by vm.products.collectAsStateWithLifecycle()
    val lang by vm.language.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var selected by remember { mutableStateOf<Long?>(null) }
    var counted  by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var showResultSheet by remember { mutableStateOf(false) }

    var resultExpected by remember { mutableStateOf(0.0) }
    var resultCounted by remember { mutableStateOf(0.0) }
    var resultDiff by remember { mutableStateOf(0.0) }
    var resultMoneyLost by remember { mutableStateOf(0.0) }

    val product: Product? = products.firstOrNull { it.id == selected }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Spacer(Modifier.height(32.dp))

        // ---- Back arrow + Title ----
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
            Text(Translations.get("stock_check", lang), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextBlack)
        }

        // Subtitle
        Text(
            text = Translations.get("stock_check_desc", lang),
            fontSize = 14.sp,
            color = TextBlack,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        Spacer(Modifier.height(24.dp))

        // ---- Pick A Product ----
        Column(Modifier.padding(horizontal = 16.dp)) {
            Text(Translations.get("pick_product", lang), fontSize = 14.sp, color = Green, modifier = Modifier.padding(bottom = 4.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = product?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
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
                            onClick = { selected = p.id; expanded = false; counted = "" })
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // ---- When a product is selected ----
        if (product != null) {
            Column(Modifier.padding(horizontal = 16.dp)) {

                Box(
                    modifier = Modifier
                        .background(Green.copy(alpha = 0.15f), RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "${Translations.get("expected_stock", lang)}: ${product.stockDisplay} ${product.unit}",
                        fontSize = 14.sp,
                        color = TextBlack
                    )
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "${Translations.get("how_much_left", lang)} (${if (product.type == "weight") "Kgs" else "Litres"})",
                    fontSize = 14.sp,
                    color = Green,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = counted,
                    onValueChange = { new -> counted = new.filter { it.isDigit() || it == '.' } },
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
                    enabled = counted.toDoubleOrNull() != null,
                    onClick = {
                        val expectedBase = product.stockBase
                        val countedBase  = (counted.toDoubleOrNull() ?: 0.0) * 1000.0
                        val diffBase     = countedBase - expectedBase

                        resultExpected = expectedBase
                        resultCounted  = countedBase
                        resultDiff     = diffBase
                        resultMoneyLost = if (diffBase < 0) (-diffBase / 1000.0) * product.buyingPrice else 0.0

                        showResultSheet = true
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Green),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text(Translations.get("check_stock", lang), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = White)
                }
            }
        }
    }

    // ---- RESULT BOTTOM SHEET ----
    if (showResultSheet && product != null) {
        ModalBottomSheet(
            onDismissRequest = { showResultSheet = false },
            containerColor = Color(0xFFE4F1E7),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .background(Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(50))
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 48.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(White, RoundedCornerShape(50))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text("${Translations.get("expected", lang)}: ${resultExpected/1000}${product.unit}", fontSize = 14.sp, color = TextBlack)
                }

                Box(
                    modifier = Modifier
                        .background(White, RoundedCornerShape(50))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text("${Translations.get("counted_by_you", lang)}: ${resultCounted/1000}${product.unit}", fontSize = 14.sp, color = TextBlack)
                }

                val diffColor = if (resultDiff < 0) Color(0xFFC62828) else Green
                val diffBgColor = if (resultDiff < 0) Color(0xFFFFEBEE) else Green.copy(alpha = 0.15f)

                Box(
                    modifier = Modifier
                        .background(diffBgColor, RoundedCornerShape(50))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "${Translations.get("difference", lang)}: ${resultDiff/1000}${product.unit}",
                        fontSize = 14.sp,
                        color = diffColor
                    )
                }

                if (resultDiff < 0) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFEBEE), RoundedCornerShape(50))
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "${Translations.get("money_lost", lang)}: Mwk ${"%,.0f".format(resultMoneyLost)}",
                            fontSize = 14.sp,
                            color = Color(0xFFC62828)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            vm.correctStock(product, resultCounted)
                            showResultSheet = false
                            onBack()
                        },
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Green)
                    ) {
                        Text(Translations.get("fix_product", lang), fontSize = 14.sp, color = White)
                    }

                    OutlinedButton(
                        onClick = { showResultSheet = false },
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(50),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = White)
                    ) {
                        Text(Translations.get("leave_it", lang), fontSize = 14.sp, color = TextBlack)
                    }
                }
            }
        }
    }
}