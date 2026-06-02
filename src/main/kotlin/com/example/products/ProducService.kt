package com.example.products

import com.example.exception.NotFoundException
import com.example.exception.ValidationException
import com.example.model.repository.ProductRepository
import com.example.products.dto.*

class ProductService(
    private val productRepository: ProductRepository
) {

    suspend fun create(request: CreateProductRequest): ProductResponse {
        if (request.name.isBlank()) {
            throw ValidationException(
                "Product name cannot be empty"
            )
        }

        if ((request.price ?: 0).toInt() < 0) {
            throw ValidationException(
                "Price cannot be negative"
            )
        }


        val product = productRepository.addProduct(request.toEntity())
        return product.toResponse()
    }

    suspend fun getById(articleNo: Int): ProductResponse {
        val product = productRepository.productById(articleNo)
            ?: throw NotFoundException(
                "Map articleNo not found"
            )
        return product.toResponse()
    }

    suspend fun getByStore(storeId: Int): List<ProductResponse> {
        return productRepository
            .productsByStoreId(storeId)
            .map { it.toResponse() }
    }

    suspend fun update(request: UpdateProductRequest): ProductResponse {

        if (request.name.isBlank()) {
            throw ValidationException(
                "Product name cannot be empty"
            )
        }

        if ((request.price ?: 0).toInt() < 0) {
            throw ValidationException(
                "Price cannot be negative"
            )
        }

        val updated = productRepository.updateProduct(request.toEntity())
        return updated.toResponse()
    }

    suspend fun delete(articleNo: Int) {
        val deleted = productRepository.removeProductById(articleNo)
        if (!deleted) {
            throw NotFoundException(
                "Product $articleNo not found"
            )
        }

    }
}