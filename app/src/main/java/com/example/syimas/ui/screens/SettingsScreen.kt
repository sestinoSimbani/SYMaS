package com.example.syimas.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.syimas.data.ShopViewModel
import com.example.syimas.ui.components.AppHeader
import com.example.syimas.ui.theme.Green
import com.example.syimas.ui.theme.TextBlack
import com.example.syimas.ui.theme.TextGray
import com.example.syimas.ui.theme.White

@Composable
fun SettingsScreen(vm: ShopViewModel) {
    val lang by vm.language.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {

        // ---- 1. Clean text header with bottom divider line ----
        AppHeader()

        // ---- 2. Main Content ----
        Column(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f) // Pushes the bottom bar down
        ) {
            // Page Title
            Text(
                text = "Settings",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // ---- Language Section ----
            Text(
                text = "Language",
                color = Green,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                border = BorderStroke(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
            ) {
                Column {
                    // English Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("English", fontSize = 15.sp)
                        Switch(
                            checked = lang == "en",
                            onCheckedChange = { if (it) vm.setLanguage("en") },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = White,
                                checkedTrackColor = Green,
                                uncheckedThumbColor = White,
                                uncheckedTrackColor = Color.LightGray
                            )
                        )
                    }

                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))

                    // Chichewa Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Chichewa", fontSize = 15.sp)
                        Switch(
                            checked = lang == "ny",
                            onCheckedChange = { if (it) vm.setLanguage("ny") },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = White,
                                checkedTrackColor = Green,
                                uncheckedThumbColor = White,
                                uncheckedTrackColor = Color.LightGray
                            )
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ---- About Section ----
            Text(
                text = "About SYIMaS",
                color = Green,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                border = BorderStroke(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "SYIMaS - Sitolo Yanga Inventory Management System Is An Inventory System For Small Agro Businesses In Malawi.",
                        fontSize = 14.sp,
                        color = TextBlack,
                        lineHeight = 20.sp
                    )

                    Spacer(Modifier.height(16.dp))

                    // Version Pill
                    Box(
                        modifier = Modifier
                            .background(
                                color = Green.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Version 1.0",
                            color = Green,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}