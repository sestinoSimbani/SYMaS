package com.example.syimas.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",                   // <-- Added default ""
    val type: String = "weight",             // <-- Added default
    val buyingPrice: Double = 0.0,           // <-- Added default
    val sellingPrice: Double = 0.0,          // <-- Added default
    val stockBase: Double = 0.0,             // <-- Added default
    val lowStockLevel: Double = 0.0,         // <-- Added default
    val quickAmounts: String = "100,200,500,1000"
) {
    // @get:Exclude tells Firebase to ignore these when saving/loading
    @get:Exclude
    val unit: String get() = if (type == "weight") "kg" else "L"

    @get:Exclude
    val stockDisplay: Double get() = stockBase / 1000.0
}