package com.example.model.repository

import com.example.model.entity.Product

interface ProductRepository {

    suspend fun productById(articleNo: Int): Product?

    suspend fun productsByStoreId(storeId: Int): List<Product>

    suspend fun addProduct(product: Product): Product

    suspend fun updateProduct(product: Product): Product

    suspend fun removeProductById(articleNo: Int)
}