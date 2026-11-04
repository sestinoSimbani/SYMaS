package com.example.syimas

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculationUnitTest {

    @Test
    fun testSalePriceByAmount_isCorrect() {
        // Formula: amount ÷ 1000 × price per kg
        val amountGrams = 200.0
        val pricePerKg = 2500.0
        val expectedPrice = 500.0

        val calculatedPrice = (amountGrams / 1000.0) * pricePerKg
        assertEquals(expectedPrice, calculatedPrice, 0.01)
    }

    @Test
    fun testAmountByMoney_isCorrect() {
        // Formula: money ÷ price per kg × 1000
        val moneyGiven = 500.0
        val pricePerKg = 2500.0
        val expectedAmountGrams = 200.0

        val calculatedAmount = (moneyGiven / pricePerKg) * 1000.0
        assertEquals(expectedAmountGrams, calculatedAmount, 0.01)
    }

    @Test
    fun testProfitCalculation_isCorrect() {
        // Formula: money earned - cost of goods
        val moneyEarned = 500.0
        val buyingPricePerKg = 2000.0
        val amountGrams = 200.0

        val costOfGoods = (amountGrams / 1000.0) * buyingPricePerKg // 400.0
        val expectedProfit = 100.0

        val calculatedProfit = moneyEarned - costOfGoods
        assertEquals(expectedProfit, calculatedProfit, 0.01)
    }
}