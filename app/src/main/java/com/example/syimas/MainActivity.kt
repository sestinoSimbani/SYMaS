package com.example.syimas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.syimas.navigation.NavGraph
import com.example.syimas.ui.theme.SYIMaSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SYIMaSTheme {
                NavGraph()
            }
        }
    }
}