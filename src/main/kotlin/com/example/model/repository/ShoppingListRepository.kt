package com.example.model.repository

import com.example.model.entity.ShoppingList

interface ShoppingListRepository {
    suspend fun shoppingListsByUser(userId: Int): List<ShoppingList>
    suspend fun addShoppingList(shoppingList: ShoppingList): ShoppingList
    suspend fun removeShoppingListById(id: Int): Boolean
}