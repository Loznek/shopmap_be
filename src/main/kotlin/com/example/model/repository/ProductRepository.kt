package com.example.model.repository

import com.example.model.entity.Product

interface ProductRepository {

    fun productById(articleNo: Int): Product?

    fun productsByStoreId(storeId: Int): List<Product>

    fun addProduct(product: Product): Product

    fun updateProduct(product: Product): Product

    fun removeProductById(articleNo: Int)
}