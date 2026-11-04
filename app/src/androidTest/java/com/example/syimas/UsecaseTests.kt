package com.example.syimas

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso // <-- NEW IMPORT
import com.example.syimas.MainActivity
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import androidx.compose.ui.test.onAllNodesWithText

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UseCaseTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // Helper function: Waits for Splash Screen to finish
    private fun waitForHomeScreen() {
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Products").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.waitForIdle()
    }

    // Helper function to hide the keyboard using Espresso
    private fun hideKeyboard() {
        try {
            Espresso.closeSoftKeyboard()
        } catch (e: Exception) {
            // Ignore if keyboard isn't open
        }
        composeTestRule.waitForIdle()
    }

    // Helper function to create a product
    private fun setupProduct(name: String, buyPrice: String = "1000", sellPrice: String = "2000", lowStock: String = "5000") {
        waitForHomeScreen()

        composeTestRule.onNodeWithText("Products").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithContentDescription("Add product").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Name").performTextInput(name)
        composeTestRule.onNodeWithText("Buying price /kg").performTextInput(buyPrice)
        composeTestRule.onNodeWithText("Selling price /kg").performTextInput(sellPrice)
        composeTestRule.onNodeWithText("Low-stock level (base units)").performTextInput(lowStock)

        hideKeyboard() // Hide keyboard so it doesn't block Save

        composeTestRule.onNodeWithText("Save").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Home").performClick()
        composeTestRule.waitForIdle()
    }

    // ==========================================
    // POSITIVE SCENARIOS
    // ==========================================

    @Test
    fun test01_manageProducts_AddEditDelete() {
        setupProduct("Test Beans")

        composeTestRule.onNodeWithText("Products").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Test Beans").assertIsDisplayed()

        composeTestRule.onNodeWithText("Edit Product").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Save").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Delete").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Yes, Delete").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Test Beans").assertDoesNotExist()
    }

    @Test
    fun test02_useCase3_restockProduct() {
        setupProduct("Maize")

        composeTestRule.onNodeWithText("Restock").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Pick a product").performClick()
        composeTestRule.onNodeWithText("Maize").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Number of bags").performTextInput("2")
        composeTestRule.onNodeWithText("kg per bag").performTextInput("50")
        composeTestRule.onNodeWithText("Total price paid (MK)").performTextInput("100000")

        hideKeyboard()
        composeTestRule.onNodeWithText("Save restock").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Products").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Stock: 100.0 kg").assertIsDisplayed()
    }

    @Test
    fun test03_useCase1_saleByAmount_QuickButton() {
        setupProduct("Sugar")

        composeTestRule.onNodeWithText("Sugar").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("500 g").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Confirm sale").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Mwk 1,000").assertIsDisplayed()
    }

    @Test
    fun test04_useCase2_saleByMoney() {
        setupProduct("Rice")

        composeTestRule.onNodeWithText("Rice").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("By money").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Money (MK)").performTextInput("500")
        hideKeyboard()

        composeTestRule.onNodeWithText("Confirm sale").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Mwk 500").assertIsDisplayed()
    }

    @Test
    fun test05_useCase4_dailySummary() {
        setupProduct("Groundnuts", lowStock = "10")

        composeTestRule.onNodeWithText("Restock").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Pick a product").performClick()
        composeTestRule.onNodeWithText("Groundnuts").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Number of bags").performTextInput("1")
        composeTestRule.onNodeWithText("kg per bag").performTextInput("1")
        composeTestRule.onNodeWithText("Total price paid (MK)").performTextInput("1000")

        hideKeyboard()
        composeTestRule.onNodeWithText("Save restock").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Home").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Groundnuts").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("500 g").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Confirm sale").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Daily Summary").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Money Earned: Mwk 1,000").assertIsDisplayed()
        composeTestRule.onNodeWithText("Profit: Mwk 500").assertIsDisplayed()
        composeTestRule.onNodeWithText("Customers Served: 1").assertIsDisplayed()
    }

    @Test
    fun test06_useCase5_stockCheck() {
        setupProduct("Beans")

        composeTestRule.onNodeWithText("Restock").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Pick a product").performClick()
        composeTestRule.onNodeWithText("Beans").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Number of bags").performTextInput("1")
        composeTestRule.onNodeWithText("kg per bag").performTextInput("10")
        composeTestRule.onNodeWithText("Total price paid (MK)").performTextInput("10000")

        hideKeyboard()
        composeTestRule.onNodeWithText("Save restock").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Home").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Stock Check").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Pick a product").performClick()
        composeTestRule.onNodeWithText("Beans").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("How much is really left? (kg)").performTextInput("8")
        hideKeyboard()

        composeTestRule.onNodeWithText("Check").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Difference: -2.0kg").assertIsDisplayed()
        composeTestRule.onNodeWithText("Money lost: MK 2000").assertIsDisplayed()

        composeTestRule.onNodeWithText("Fix stock").performClick()
        composeTestRule.waitForIdle()
    }

    // ==========================================
    // NEGATIVE SCENARIOS
    // ==========================================

    @Test
    fun test07_negative_emptyProductName() {
        waitForHomeScreen()

        composeTestRule.onNodeWithText("Products").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Add product").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Buying price /kg").performTextInput("1000")
        composeTestRule.onNodeWithText("Selling price /kg").performTextInput("2000")

        hideKeyboard()
        composeTestRule.onNodeWithText("Save").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Add product").assertIsDisplayed()
    }

    @Test
    fun test08_negative_preventOverselling() {
        setupProduct("Oil", sellPrice = "5000", lowStock = "100")

        composeTestRule.onNodeWithText("Oil").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Amount").performTextInput("1000")
        hideKeyboard()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Not enough stock!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Confirm sale").assertIsNotEnabled()
    }

    @Test
    fun test09_negative_cancelClearHistory() {
        setupProduct("History Test")

        composeTestRule.onNodeWithText("History Test").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("100 g").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Confirm sale").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("History").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithContentDescription("Clear History").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Cancel").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Total Sales", substring = true).assertIsDisplayed()
    }

    // 10. USE CASE: Switch Language to Chichewa
    @Test
    fun test10_switchLanguageToChichewa() {
        waitForHomeScreen()

        // Go to Settings
        composeTestRule.onNodeWithText("Settings").performClick()
        composeTestRule.waitForIdle()

        // Toggle Chichewa
        composeTestRule.onNodeWithText("Chichewa").performClick()
        composeTestRule.waitForIdle()

        // Verify the bottom navigation translated to Chichewa
        composeTestRule.onNodeWithText("Kunyumba").assertIsDisplayed() // Home
        composeTestRule.onNodeWithText("Katundu").assertIsDisplayed()  // Products
    }
}