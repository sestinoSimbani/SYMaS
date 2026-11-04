package com.example.syimas.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import com.example.syimas.ui.theme.Green
import com.example.syimas.ui.theme.TextGray

/**
 * Centered "nothing here yet" block.
 * Draws two arcs (cut circles) around an illustration, with a caption below.
 *
 * To swap in a different illustration, pass a different @DrawableRes.
 */
@Composable
fun EmptyState(
    @DrawableRes illustration: Int,
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(220.dp),
            contentAlignment = Alignment.Center
        ) {
            // ---- two cut-circle arcs drawn with Canvas ----
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 1.5.dp.toPx()

                // Outer arc: top-left and bottom-right quadrant
                drawArc(
                    color = Green.copy(alpha = 0.20f),
                    startAngle = 100f,
                    sweepAngle = 200f,
                    useCenter = false,
                    topLeft = Offset.Zero,
                    size = Size(size.width, size.height),
                    style = Stroke(width = strokeWidth)
                )

                // Inner arc: shifted inward, cut on the opposite sides
                val inset = size.width * 0.18f
                drawArc(
                    color = Green.copy(alpha = 0.12f),
                    startAngle = 280f,
                    sweepAngle = 200f,
                    useCenter = false,
                    topLeft = Offset(inset, inset),
                    size = Size(size.width - inset * 2, size.height - inset * 2),
                    style = Stroke(width = strokeWidth)
                )
            }

            // ---- the illustration in the middle ----
            Image(
                painter = painterResource(id = illustration),
                contentDescription = null,
                modifier = Modifier.size(141.dp)
            )
        }

        Spacer(Modifier.height(24.dp))
        Text(
            text = message,
            color = TextGray,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
    }
}