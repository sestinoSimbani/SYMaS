package com.example.syimas.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.syimas.R

// Groups the two .ttf files into one FontFamily.
// Whenever a TextStyle uses this family, Compose picks the file
// matching the requested weight (Regular for Normal, Bold for Bold).
val AlteHaas = FontFamily(
    Font(R.font.alte_haas_grotesk_regular, FontWeight.Normal),
    Font(R.font.alte_haas_grotesk_bold,    FontWeight.Bold)
)

// App-wide typography, all using Alte Haas.
val AppTypography = Typography(
    displayLarge  = TextStyle(fontFamily = AlteHaas, fontWeight = FontWeight.Bold,   fontSize = 32.sp),
    titleLarge    = TextStyle(fontFamily = AlteHaas, fontWeight = FontWeight.Bold,   fontSize = 22.sp),
    titleMedium   = TextStyle(fontFamily = AlteHaas, fontWeight = FontWeight.Bold,   fontSize = 18.sp),
    bodyLarge     = TextStyle(fontFamily = AlteHaas, fontWeight = FontWeight.Normal, fontSize = 16.sp),
    bodyMedium    = TextStyle(fontFamily = AlteHaas, fontWeight = FontWeight.Normal, fontSize = 14.sp),
    labelLarge    = TextStyle(fontFamily = AlteHaas, fontWeight = FontWeight.Bold,   fontSize = 14.sp),
    labelSmall    = TextStyle(fontFamily = AlteHaas, fontWeight = FontWeight.Normal, fontSize = 12.sp)
)