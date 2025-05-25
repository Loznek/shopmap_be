package com.example.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class ShoppingList(
    val id: Int?, // Article_No
    val userId: Int,
    val name: String,
)