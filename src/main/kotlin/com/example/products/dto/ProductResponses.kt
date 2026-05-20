package com.example.products.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val articleNo: Int?,
    val name: String,
    val size: String? = null,
    val departmentId: Int? = null,
    val position: String? = null,
    val storeId: Int,
    val price: Double? = null
)