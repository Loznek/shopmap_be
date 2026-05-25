package com.example.repository

import com.example.model.entity.ShoppingListItem

interface ShoppingListItemRepository {

    suspend fun addShoppingListItem(
        item: ShoppingListItem
    ): ShoppingListItem

    suspend fun getShoppingListItems(
        shoppingListId: Int
    ): List<ShoppingListItem>

    suspend fun updateShoppingListItem(
        item: ShoppingListItem
    ): ShoppingListItem

    suspend fun deleteShoppingListItem(
        itemId: Int
    )
}