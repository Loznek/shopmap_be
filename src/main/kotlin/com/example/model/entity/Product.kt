package com.example.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val articleNo: Int, // Article_No
    val name: String,
    val size: Int,
    val shelfId: Int,
    val price: Float
)