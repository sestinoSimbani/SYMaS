package com.example.syimas.data
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun all(): Flow<List<Product>>                     // auto-updates the UI

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun byId(id: Long): Product?

    @Insert suspend fun insert(p: Product): Long
    @Update suspend fun update(p: Product)
    @Delete suspend fun delete(p: Product)
}

@Dao
interface SaleDao {
    @Insert suspend fun insert(s: Sale)

    @Query("SELECT * FROM sales ORDER BY timestamp DESC")
    fun all(): Flow<List<Sale>>

    @Query("SELECT * FROM sales WHERE timestamp BETWEEN :from AND :to ORDER BY timestamp DESC")
    fun between(from: Long, to: Long): Flow<List<Sale>>

    @Query("DELETE FROM sales")
    suspend fun clearAllSales()
}