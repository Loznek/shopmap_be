package com.example.model.repository

import com.example.model.entity.ShoppingListItem

interface ShoppingListItemRepository {
    suspend fun itemsByShoppingList(shoppingListId: Int): List<ShoppingListItem>
    suspend fun addShoppingListItem(item: ShoppingListItem): ShoppingListItem
    suspend fun removeShoppingListItemById(id: Int): Boolean
    suspend fun updateShoppingListItem(item: ShoppingListItem): ShoppingListItem
}