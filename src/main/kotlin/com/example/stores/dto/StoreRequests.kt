package com.example.stores.dto

import ShoppingListItemResponse
import com.example.model.entity.ShoppingListItem
import com.example.products.dto.ProductResponse
import com.example.shoppingList.dto.CreateShoppingListItemRequest
import kotlinx.serialization.Serializable

@Serializable
data class CreateStoreRequest(
    val name: String,
    val location: String?
)


