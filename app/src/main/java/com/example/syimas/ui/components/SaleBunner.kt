package com.example.syimas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.syimas.ui.theme.Green
import com.example.syimas.ui.theme.White
import com.example.syimas.ui.utils.Translations // <--- ADDED MISSING IMPORT
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SaleBanner(totalToday: Double, lang: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(Green, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Column(Modifier.align(Alignment.CenterStart)) {
            // FIXED: Wrapped in Text() and removed the duplicate hardcoded English text
            Text(
                text = Translations.get("today_sales", lang),
                color = White.copy(alpha = 0.85f),
                fontSize = 14.sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Mwk " + NumberFormat.getNumberInstance(Locale.US).format(totalToday.toInt()),
                color = White, fontSize = 30.sp
            )
        }
        // Simple "SALE" badge (rotated) – easy substitute for the starburst shape
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .rotate(15f)
                .background(White, RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text("SALE", color = Green, fontSize = 12.sp)
        }
    }
}