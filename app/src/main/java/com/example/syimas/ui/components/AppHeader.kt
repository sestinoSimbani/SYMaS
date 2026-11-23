package com.example.syimas.ui.components

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.syimas.ui.theme.Green
import com.example.syimas.ui.theme.White

@Composable
fun AppHeader() {
    val context = LocalContext.current
    val isOnline = remember { isNetworkAvailable(context) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Green)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("SYIMaS", fontSize = 24.sp, color = White)
            Text("Sitolo Yanga Inventory System", fontSize = 14.sp, color = White)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Show cloud icon based on connectivity
            Icon(
                imageVector = if (isOnline) Icons.Filled.CloudDone else Icons.Filled.CloudOff,
                contentDescription = "Sync Status",
                tint = White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Profile",
                tint = White,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

// check internet connection
private fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}