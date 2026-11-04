package com.example.syimas.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.syimas.ui.theme.Green
import com.example.syimas.ui.theme.TextGray
import com.example.syimas.ui.theme.White
import com.example.syimas.ui.utils.Translations

data class BottomItem(val route: String, val labelKey: String, val icon: ImageVector)

val bottomItems = listOf(
    BottomItem("home",     "home",     Icons.Filled.Home),
    BottomItem("products", "products", Icons.Filled.Inventory2),
    BottomItem("restock",  "restock",  Icons.Filled.Refresh),
    BottomItem("settings", "settings", Icons.Filled.Settings),
)

@Composable
fun BottomBar(currentRoute: String?, lang: String, onSelect: (String) -> Unit) {
    NavigationBar(
        containerColor = Color(0xFFF2F7F3),
        contentColor = TextGray
    ) {
        bottomItems.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick  = { onSelect(item.route) },
                icon     = { Icon(item.icon, contentDescription = item.labelKey, modifier = Modifier.size(24.dp)) },
                label    = { Text(Translations.get(item.labelKey, lang), fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                colors   = NavigationBarItemDefaults.colors(
                    selectedIconColor   = White,
                    selectedTextColor   = Green,
                    unselectedIconColor = TextGray,
                    unselectedTextColor = TextGray,
                    indicatorColor      = Green
                )
            )
        }
    }
}