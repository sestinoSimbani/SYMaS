package com.example.syimas.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.syimas.data.ShopViewModel
import com.example.syimas.ui.components.BottomBar
import com.example.syimas.ui.screens.*

object Routes {
    const val SPLASH   = "splash"
    const val HOME     = "home"
    const val PRODUCTS = "products"
    const val RESTOCK  = "restock"
    const val SETTINGS = "settings"
    const val SUMMARY  = "summary"
    const val HISTORY  = "history"
    const val STOCK    = "stock"
}

@Composable
fun NavGraph() {
    val nav = rememberNavController()
    val vm: ShopViewModel = viewModel()
    val lang by vm.language.collectAsStateWithLifecycle()

    val backStack by nav.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Routes.HOME, Routes.PRODUCTS, Routes.RESTOCK, Routes.SETTINGS
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(currentRoute, lang) { route ->
                    nav.navigate(route) {
                        popUpTo(Routes.HOME) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0) // <--- ADD THIS LINE
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = Routes.SPLASH,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.SPLASH) {
                SplashScreen {
                    nav.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            }
            composable(Routes.HOME) {
                HomeScreen(
                    vm,
                    onOpenSummary    = { nav.navigate(Routes.SUMMARY) },
                    onOpenStockCheck = { nav.navigate(Routes.STOCK) },
                    onOpenHistory    = { nav.navigate(Routes.HISTORY) }
                )
            }
            composable(Routes.PRODUCTS) {
                ProductsScreen(
                    vm,
                    onBack = {
                        nav.navigate(Routes.HOME) { launchSingleTop = true }
                    }
                )
            }
            composable(Routes.RESTOCK) {
                RestockScreen(
                    vm,
                    onNavigateHome = {
                        // This navigates to the Home tab when "Check Product" is clicked
                        nav.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(Routes.SETTINGS) { SettingsScreen(vm) }
            composable(Routes.SUMMARY)  { DailySummaryScreen(vm, onBack = { nav.popBackStack() }) }
            composable(Routes.HISTORY)  { HistoryScreen(vm, onBack = { nav.popBackStack() }) }
            composable(Routes.STOCK)    { StockCheckScreen(vm, onBack = { nav.popBackStack() }) }
        }
    }
}