package com.example.syimas.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SyncRepository(private val db: AppDatabase) {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        if (auth.currentUser == null) {
            auth.signInAnonymously().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("SyncRepo", "Anonymous login successful: ${auth.currentUser?.uid}")
                    startSync()
                } else {
                    Log.e("SyncRepo", "Anonymous login FAILED", task.exception)
                }
            }
        } else {
            startSync()
        }
    }

    private fun startSync() {
        val userId = auth.currentUser?.uid ?: return
        val productsRef = firestore.collection("users").document(userId).collection("products")

        // 2. Push local Room changes to Firestore
        scope.launch {
            db.productDao().all().collect { localProducts ->
                Log.d("SyncRepo", "Found ${localProducts.size} products to sync.")

                localProducts.forEach { product ->
                    productsRef.document(product.id.toString()).set(product)
                        .addOnSuccessListener {
                            Log.d("SyncRepo", "Successfully synced: ${product.name}")
                        }
                        .addOnFailureListener { e ->
                            Log.e("SyncRepo", "Failed to sync: ${product.name}", e)
                        }
                }
            }
        }

        // 3. Pull Cloud changes to Room
        productsRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("SyncRepo", "Listen failed", error)
                return@addSnapshotListener
            }
            if (snapshot != null && !snapshot.metadata.hasPendingWrites()) {
                scope.launch {
                    try {
                        val remoteProducts = snapshot.documents.mapNotNull { it.toObject(Product::class.java) }
                        if (remoteProducts.isNotEmpty()) {
                            db.productDao().insertAll(remoteProducts)
                            Log.d("SyncRepo", "Pulled ${remoteProducts.size} products from Cloud.")
                        }
                    } catch (e: Exception) {
                        Log.e("SyncRepo", "Error mapping cloud data to Room", e)
                    }
                }
            }
        }
    }
}