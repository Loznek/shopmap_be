package com.example.model.repository

import com.example.model.entity.Product

interface ProductRepository {
    suspend fun productsByStoreId(storeId: Int): List<Product>
    suspend fun addProduct(product: Product): Product
    suspend fun removeProductById(id: Int): Boolean
    suspend fun updateProduct(product: Product): Product
    suspend fun productById(id: Int): Product?
}