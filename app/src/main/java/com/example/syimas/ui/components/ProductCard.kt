package com.example.syimas.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.syimas.data.Product
import com.example.syimas.ui.theme.Green
import com.example.syimas.ui.theme.LightGray
import com.example.syimas.ui.theme.TextBlack

@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        // Grey image area (placeholder for a product photo)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(LightGray, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Use the first letter until you add real images
            Text(
                product.name.take(1).uppercase(),
                fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Green
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(product.name, fontWeight = FontWeight.Bold, color = TextBlack, fontSize = 15.sp)
        Text(
            "${formatStock(product.stockDisplay)}${product.unit} Remaining",
            color = Green, fontSize = 13.sp
        )
    }
}

// Formats "32.0" → "32", "32.5" → "32.5"
private fun formatStock(v: Double): String =
    if (v % 1.0 == 0.0) v.toInt().toString() else String.format("%.1f", v)