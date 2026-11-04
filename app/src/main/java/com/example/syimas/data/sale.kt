package com.example.syimas.data
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sales")
data class Sale(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val productName: String,       // denormalised so history still shows the name
    val amountBase: Double,        // in g or ml
    val moneyEarned: Double,       // MK
    val costOfGoods: Double,       // MK
    val timestamp: Long            // System.currentTimeMillis()
) {
    val profit: Double get() = moneyEarned - costOfGoods
}