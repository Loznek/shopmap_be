package com.example.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class ShoppingListItem(
    val itemId: Int?,
    val shoppingListId: Int,
    val shoppingItemName: String,
    val attributes: String
)