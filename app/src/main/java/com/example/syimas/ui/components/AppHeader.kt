package com.example.syimas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.syimas.ui.theme.Green
import com.example.syimas.ui.theme.White

@Composable
fun AppHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Green)
            .statusBarsPadding() // <--- THIS pushes the text down but keeps the green background at the very top
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "SYIMaS",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
            Text(
                text = "Sitolo Yanga Inventory System",
                fontSize = 14.sp,
                color = White
            )
        }

        Icon(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = "Profile",
            tint = White,
            modifier = Modifier.size(36.dp)
        )
    }
}