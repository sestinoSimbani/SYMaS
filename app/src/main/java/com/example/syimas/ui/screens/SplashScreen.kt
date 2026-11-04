package com.example.syimas.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.syimas.ui.theme.Green
import com.example.syimas.ui.theme.White
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onDone: () -> Unit) {
    // 1. Keep the 2-second delay before navigating to the Home screen
    LaunchedEffect(Unit) {
        delay(2000)
        onDone()
    }

    // 2. Infinite Animation for the SYIMaS word (breathing effect)
    val mainTransition = rememberInfiniteTransition(label = "mainPulse")
    val scale by mainTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    val alpha by mainTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    // 3. Infinite Animation for the Loading Dots (wave effect)
    val dotsTransition = rememberInfiniteTransition(label = "dotsWave")

    // 4. The main layout container (Clean Green Background)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Green)
    ) {

        // ---- Center Content (Logo & Text) ----
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // The animated SYIMaS text
            Text(
                text = "SYIMaS",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = White,
                modifier = Modifier
                    .scale(scale) // Pulsing scale
                    .alpha(alpha) // Pulsing opacity
            )

            Spacer(Modifier.height(12.dp))

            // The subtitle
            Text(
                text = "Sitolo Yanga Inventory Management System",
                fontSize = 16.sp,
                color = White,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }

        // ---- Bottom Loading Indicator (Animated Dots & Text) ----
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // The 5 animated dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Loop 5 times to create the dots
                for (i in 0 until 5) {
                    // Stagger the animation of each dot by 120 milliseconds
                    // FIXED: Removed the "L" so it is an Int, not a Long
                    val delay = i * 120

                    // Scale animation (0.8f to 1.4f)
                    val dotScale by dotsTransition.animateFloat(
                        initialValue = 0.8f,
                        targetValue = 1.4f,
                        animationSpec = infiniteRepeatable(
                            animation = keyframes {
                                durationMillis = 800
                                0.8f at 0
                                1.4f at 400
                                0.8f at 800
                            },
                            repeatMode = RepeatMode.Restart,
                            initialStartOffset = StartOffset(delay) // Now accepts the Int correctly
                        ),
                        label = "dotScale"
                    )

                    // Opacity animation (0.3f to 1f)
                    val dotAlpha by dotsTransition.animateFloat(
                        initialValue = 0.3f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = keyframes {
                                durationMillis = 800
                                0.3f at 0
                                1f at 400
                                0.3f at 800
                            },
                            repeatMode = RepeatMode.Restart,
                            initialStartOffset = StartOffset(delay) // Now accepts the Int correctly
                        ),
                        label = "dotAlpha"
                    )

                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .scale(dotScale) // Apply the scale wave
                            .alpha(dotAlpha) // Apply the opacity wave
                            .background(White, CircleShape)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // The loading text
            Text(
                text = "Getting Ready For You...",
                fontSize = 13.sp,
                color = White.copy(alpha = 0.8f)
            )
        }
    }
}