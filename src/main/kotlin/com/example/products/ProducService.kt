package com.example.products

import com.example.model.repository.ProductRepository
import com.example.products.dto.*

class ProductService(
    private val productRepository: ProductRepository
) {

    suspend fun create(request: CreateProductRequest): ProductResponse {
        val product = productRepository.addProduct(request.toEntity())
        return product.toResponse()
    }

    suspend fun getById(articleNo: Int): ProductResponse {
        val product = productRepository.productById(articleNo)
            ?: throw IllegalArgumentException("Product not found")

        return product.toResponse()
    }

    suspend fun getByStore(storeId: Int): List<ProductResponse> {
        return productRepository
            .productsByStoreId(storeId)
            .map { it.toResponse() }
    }

    suspend fun update(request: UpdateProductRequest): ProductResponse {
        val updated = productRepository.updateProduct(request.toEntity())
        return updated.toResponse()
    }

    suspend fun delete(articleNo: Int) {
        productRepository.removeProductById(articleNo)
    }
}