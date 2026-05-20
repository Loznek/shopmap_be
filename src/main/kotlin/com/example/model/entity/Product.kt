package com.example.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val articleNo: Int? = null,
    val name: String,
    val size: String? = null,
    val departmentId: Int? = null,
    val position: ProductPosition? = null,
    val storeId: Int,
    val price: Double? = null
)

@Serializable
enum class ProductPosition {
    LEFT,
    RIGHT,
    TOP,
    BOTTOM
}