package com.example.syimas.data
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, // auto-generated id
    val name: String,                                   // e.g. "Red Beans"
    val type: String,                                   // "weight" or "volume"
    val buyingPrice: Double,                            // per kg (or per litre)
    val sellingPrice: Double,                           // per kg (or per litre)
    val stockBase: Double,                              // in g (weight) or ml (volume)
    val lowStockLevel: Double,                          // in g / ml
    val quickAmounts: String = "100,200,500,1000"       // comma-separated base units
) {
    // Helper: unit label for the UI ("kg" or "L")
    val unit: String get() = if (type == "weight") "kg" else "L"
    // Helper: stock as a display value (g→kg, ml→L)
    val stockDisplay: Double get() = stockBase / 1000.0
}