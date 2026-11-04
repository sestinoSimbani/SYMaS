package com.example.syimas.data
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

class ShopViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.get(app)
    private val productsDao = db.productDao()
    private val salesDao    = db.saleDao()

    // ----- State the UI observes -----------------------------------
    val products: StateFlow<List<Product>> =
        productsDao.all().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val todaySales: StateFlow<List<Sale>> =
        salesDao.between(startOfToday(), endOfToday())
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val allSales: StateFlow<List<Sale>> =
        salesDao.all().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Language toggle (in memory only for simplicity)
    private val _language = MutableStateFlow("en")
    val language: StateFlow<String> = _language
    fun setLanguage(l: String) { _language.value = l }

    // ----- Actions the UI calls ------------------------------------
    fun addProduct(p: Product) = viewModelScope.launch { productsDao.insert(p) }
    fun updateProduct(p: Product) = viewModelScope.launch { productsDao.update(p) }
    fun deleteProduct(p: Product) = viewModelScope.launch { productsDao.delete(p) }

    /** Restock: add `bags` bags of `kgPerBag` kg (or L) at `totalPrice` MK. */
    fun restock(product: Product, bags: Int, sizePerBag: Double, totalPrice: Double) {
        viewModelScope.launch {
            val addedBase = bags * sizePerBag * 1000.0   // convert to g or ml
            val newStock  = product.stockBase + addedBase
            // new average buying price (simple weighted average)
            val bagCount = product.stockBase / (sizePerBag * 1000.0)
            val oldTotal = product.buyingPrice * product.stockDisplay
            val newPrice = if (newStock > 0)
                (oldTotal + totalPrice) / (newStock / 1000.0)
            else product.buyingPrice

            productsDao.update(product.copy(
                stockBase   = newStock,
                buyingPrice = newPrice
            ))
        }
    }

    /** Record a sale by amount (in g or ml). */
    fun recordSale(product: Product, amountBase: Double) {
        viewModelScope.launch {
            if (amountBase <= 0 || amountBase > product.stockBase) return@launch
            val money = amountBase / 1000.0 * product.sellingPrice
            val cost  = amountBase / 1000.0 * product.buyingPrice
            salesDao.insert(Sale(
                productId = product.id,
                productName = product.name,
                amountBase = amountBase,
                moneyEarned = money,
                costOfGoods = cost,
                timestamp = System.currentTimeMillis()
            ))
            productsDao.update(product.copy(stockBase = product.stockBase - amountBase))
        }
    }

    /** Record a sale by money: customer paid `money` MK. */
    fun recordSaleByMoney(product: Product, money: Double) {
        if (product.sellingPrice <= 0) return
        val amountBase = money / product.sellingPrice * 1000.0
        recordSale(product, amountBase.coerceAtMost(product.stockBase))
    }

    /** Correct stock after a stock check. */
    fun correctStock(product: Product, realBase: Double) {
        viewModelScope.launch {
            productsDao.update(product.copy(stockBase = realBase))
        }
    }

    fun clearHistory() = viewModelScope.launch {
        salesDao.clearAllSales()
    }

    // ----- Time helpers --------------------------------------------
    private fun startOfToday(): Long {
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, 0); c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0);      c.set(Calendar.MILLISECOND, 0)
        return c.timeInMillis
    }
    private fun endOfToday(): Long = startOfToday() + 86_400_000L  // +24h
}
