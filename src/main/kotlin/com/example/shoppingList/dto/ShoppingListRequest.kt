package com.example.shoppingList.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateShoppingListRequest(
    val name: String,
    val items: List<CreateShoppingListItemRequest>
)

@Serializable
data class CreateShoppingListItemRequest(
    val shoppingItemName: String,
    val attributes: String
)


data class ShoppingListItemRequest(
    val id: Int,
    val shoppingItemName: String,
    val attributes: String
)