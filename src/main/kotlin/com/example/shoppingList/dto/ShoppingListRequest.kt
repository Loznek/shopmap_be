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


@Serializable
data class UpdateShoppingListRequest(
    val id: Int,
    val name: String,
    val items: List<CreateShoppingListItemRequest>
)


