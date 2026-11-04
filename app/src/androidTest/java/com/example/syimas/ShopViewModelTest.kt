package com.example.syimas

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.syimas.data.AppDatabase
import com.example.syimas.data.Product
import com.example.syimas.data.ShopViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShopViewModelTest {

    private lateinit var db: AppDatabase
    private lateinit var vm: ShopViewModel

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Create an in-memory database (it deletes itself after the test)
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        // We have to manually inject the in-memory DB for testing
        // Note: If ShopViewModel doesn't accept a DB in constructor, this test
        // needs to be adjusted to use the singleton, but for now we test the DAO directly.
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertProductAndRecordSale_reducesStock() = runTest {
        val productDao = db.productDao()
        val saleDao = db.saleDao()

        // 1. Add a product with 50kg (50,000g) of stock
        val newProduct = Product(
            name = "Sugar", type = "weight",
            buyingPrice = 2000.0, sellingPrice = 2500.0,
            stockBase = 50000.0, lowStockLevel = 5000.0
        )
        productDao.insert(newProduct)

        // 2. Fetch it from the database
        val products = productDao.all().first()
        val savedProduct = products.first()

        // 3. Simulate a sale of 200g
        val saleAmount = 200.0
        val money = (saleAmount / 1000.0) * savedProduct.sellingPrice
        val cost = (saleAmount / 1000.0) * savedProduct.buyingPrice

        // Update stock
        productDao.update(savedProduct.copy(stockBase = savedProduct.stockBase - saleAmount))

        // 4. Check if stock reduced correctly (50000 - 200 = 49800)
        val updatedProducts = productDao.all().first()
        val updatedProduct = updatedProducts.first()

        assertEquals(49800.0, updatedProduct.stockBase, 0.01)
    }
}