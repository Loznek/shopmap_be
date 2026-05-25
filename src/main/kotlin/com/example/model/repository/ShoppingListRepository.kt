package com.example.repository

import com.example.model.entity.ShoppingList

interface ShoppingListRepository {

    suspend fun addShoppingList(
        shoppingList: ShoppingList
    ): ShoppingList

    suspend fun getShoppingList(
        id: Int
    ): ShoppingList?

    suspend fun getShoppingListsByUser(
        userId: Int
    ): List<ShoppingList>

    suspend fun updateShoppingList(
        shoppingList: ShoppingList
    ): ShoppingList

    suspend fun deleteShoppingList(
        id: Int
    )
}