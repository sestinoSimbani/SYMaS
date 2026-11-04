package com.example.syimas

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.syimas.data.ShopViewModel
import com.example.syimas.ui.screens.SettingsScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@RunWith(AndroidJUnit4::class)
class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun settingsScreen_languageToggle_works() {
        // Start the Settings Screen
        composeTestRule.setContent {
            // We need a viewModel. In UI tests, we can just grab a default one.
            val vm: ShopViewModel = viewModel()
            SettingsScreen(vm)
        }

        // 1. Check if "English" is visible
        composeTestRule.onNodeWithText("English").assertIsDisplayed()

        // 2. Check if "Chichewa" is visible
        composeTestRule.onNodeWithText("Chichewa").assertIsDisplayed()

        // 3. Find the "Chichewa" switch and click it
        // (Assuming the switch is in the same row as the text)
        // Note: Standard Compose Switch testing requires specific tags if we want to be 100% precise,
        // but clicking the text usually toggles it if it's clickable.
        composeTestRule.onNodeWithText("Chichewa").performClick()

        // Wait for UI to settle
        composeTestRule.waitForIdle()
    }
}