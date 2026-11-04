package com.example.syimas.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Edit
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
import com.example.syimas.R
import com.example.syimas.data.Product
import com.example.syimas.data.ShopViewModel
import com.example.syimas.ui.components.AppHeader
import com.example.syimas.ui.components.EmptyState
import com.example.syimas.ui.theme.Green
import com.example.syimas.ui.theme.TextBlack
import com.example.syimas.ui.theme.White
import com.example.syimas.ui.utils.Translations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(vm: ShopViewModel, onBack: () -> Unit) {
    val products by vm.products.collectAsStateWithLifecycle()
    val lang     by vm.language.collectAsStateWithLifecycle()

    var editing by remember { mutableStateOf<Product?>(null) }
    var showNew by remember { mutableStateOf(false) }
    var deletingProduct by remember { mutableStateOf<Product?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showNew = true },
                containerColor = Green,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add product", tint = White)
            }
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            AppHeader()

            Column(Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                Text(
                    text = Translations.get("products", lang),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack
                )
                Text(
                    text = Translations.get("manage_products", lang),
                    fontSize = 14.sp,
                    color = TextBlack,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (products.isEmpty()) {
                EmptyState(
                    illustration = R.drawable.empty_products,
                    message = Translations.get("add_products_here", lang),
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(products, key = { it.id }) { p ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = White),
                            border = BorderStroke(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .background(Green.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = p.name.take(1).uppercase(),
                                            fontSize = 32.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Green
                                        )
                                    }

                                    Spacer(Modifier.width(16.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Start,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = p.name,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextBlack,
                                                modifier = Modifier.weight(1f, fill = false)
                                            )
                                            Spacer(Modifier.width(12.dp))
                                            Text(
                                                text = "${Translations.get("stock", lang)}: ${p.stockDisplay} ${p.unit}",
                                                fontSize = 14.sp,
                                                color = TextBlack
                                            )
                                        }

                                        Spacer(Modifier.height(8.dp))

                                        Box(
                                            modifier = Modifier
                                                .background(Green.copy(alpha = 0.15f), RoundedCornerShape(50))
                                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = "${Translations.get("sale_price", lang)}: Mwk ${p.sellingPrice.toInt()}/${p.unit}",
                                                fontSize = 14.sp,
                                                color = TextBlack
                                            )
                                        }
                                    }
                                }

                                Spacer(Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Button(
                                        onClick = { editing = p },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp),
                                        shape = RoundedCornerShape(50),
                                        colors = ButtonDefaults.buttonColors(containerColor = Green),
                                        contentPadding = PaddingValues(horizontal = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Edit,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(Modifier.width(6.dp))
                                        Text(Translations.get("edit_product", lang), fontSize = 13.sp, maxLines = 1, softWrap = false)
                                    }

                                    Button(
                                        onClick = { deletingProduct = p },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp),
                                        shape = RoundedCornerShape(50),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                                        contentPadding = PaddingValues(horizontal = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(Modifier.width(6.dp))
                                        Text(Translations.get("delete", lang), fontSize = 13.sp, maxLines = 1, softWrap = false)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showNew) {
        ProductForm(
            existing = null,
            lang = lang,
            onSave = { newProduct ->
                vm.addProduct(newProduct)
                showNew = false
                showSuccessDialog = true
            },
            onCancel = { showNew = false }
        )
    }

    editing?.let { product ->
        ProductForm(
            existing = product,
            lang = lang,
            onSave = { updated -> vm.updateProduct(updated); editing = null },
            onCancel = { editing = null }
        )
    }

    deletingProduct?.let { product ->
        AlertDialog(
            onDismissRequest = { deletingProduct = null },
            title = { Text(Translations.get("delete_product", lang)) },
            text = { Text("${Translations.get("delete_product_desc", lang)} '${product.name}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        vm.deleteProduct(product)
                        deletingProduct = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                ) {
                    Text(Translations.get("yes_delete", lang))
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingProduct = null }) {
                    Text(Translations.get("cancel", lang), color = TextBlack)
                }
            }
        )
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text(Translations.get("product_added_success", lang), fontWeight = FontWeight.Bold) },
            text = { Text(Translations.get("check_product_now", lang)) },
            confirmButton = {
                Button(
                    onClick = { showSuccessDialog = false },
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

@Composable
fun ProductForm(
    existing: Product?,
    lang: String,
    onSave: (Product) -> Unit,
    onCancel: () -> Unit = {}
) {
    var name by remember { mutableStateOf(existing?.name ?: "") }
    var type by remember { mutableStateOf(existing?.type ?: "weight") }
    var buy  by remember { mutableStateOf(existing?.buyingPrice?.toString()  ?: "") }
    var sell by remember { mutableStateOf(existing?.sellingPrice?.toString() ?: "") }
    var low  by remember { mutableStateOf(existing?.lowStockLevel?.toString() ?: "1000") }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(if (existing == null) Translations.get("add_product", lang) else Translations.get("edit_product_title", lang)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(name, { name = it }, label = { Text(Translations.get("name", lang)) })
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(type == "weight", { type = "weight" }, { Text(Translations.get("weight_kg", lang)) })
                    FilterChip(type == "volume", { type = "volume" }, { Text(Translations.get("volume_l", lang)) })
                }
                OutlinedTextField(
                    buy, { buy = it },
                    label = { Text(Translations.get("buying_price", lang)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    sell, { sell = it },
                    label = { Text(Translations.get("selling_price", lang)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    low, { low = it },
                    label = { Text(Translations.get("low_stock_level", lang)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (name.isBlank()) return@Button
                val base = existing ?: Product(
                    name = "", type = "weight",
                    buyingPrice = 0.0, sellingPrice = 0.0,
                    stockBase = 0.0, lowStockLevel = 0.0
                )
                val result = base.copy(
                    name = name,
                    type = type,
                    buyingPrice = buy.toDoubleOrNull() ?: 0.0,
                    sellingPrice = sell.toDoubleOrNull() ?: 0.0,
                    lowStockLevel = low.toDoubleOrNull() ?: 0.0
                )
                onSave(result)
            }) { Text(Translations.get("save", lang)) }
        },
        dismissButton = { TextButton(onClick = onCancel) { Text(Translations.get("cancel", lang), color = TextBlack) } }
    )
}