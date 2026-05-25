package com.example.shoppingList.dto

import ShoppingListItemResponse
import ShoppingListResponse
import com.example.model.entity.ShoppingList
import com.example.model.entity.ShoppingListItem

fun ShoppingListItem.toResponse() =
    ShoppingListItemResponse(
        itemId = itemId ?: 0,
        shoppingItemName = shoppingItemName,
        attributes = attributes
    )

fun ShoppingList.toResponse(
    items: List<ShoppingListItem>
) = ShoppingListResponse(
    id = id ?: 0,
    userId = userId,
    name = name,
    items = items.map { it.toResponse() }
)

fun CreateShoppingListItemRequest.toModel(
    shoppingListId: Int = 0
) = ShoppingListItem(
    itemId = null,
    shoppingListId = shoppingListId,
    shoppingItemName = shoppingItemName,
    attributes = attributes
)