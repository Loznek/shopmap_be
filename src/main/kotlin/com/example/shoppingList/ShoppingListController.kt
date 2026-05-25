package com.example.controller

import FirebaseUserPrincipal
import ShoppingListService
import com.example.shoppingList.dto.toModel
import com.example.shoppingList.dto.CreateShoppingListRequest


class ShoppingListController(
    private val shoppingListService: ShoppingListService
) {

    suspend fun getLists(
        principal: FirebaseUserPrincipal
    ) =
        shoppingListService.getLists(principal)

    suspend fun getList(
        principal: FirebaseUserPrincipal,
        id: Int
    ) =
        shoppingListService.getList(
            principal,
            id
        )

    suspend fun createList(
        principal: FirebaseUserPrincipal,
        request: CreateShoppingListRequest
    ) =
        shoppingListService.createList(
            principal,
            request.name,
            request.items.map { it.toModel() }
        )

    suspend fun deleteList(
        principal: FirebaseUserPrincipal,
        id: Int
    ) =
        shoppingListService.deleteList(
            principal,
            id
        )
}