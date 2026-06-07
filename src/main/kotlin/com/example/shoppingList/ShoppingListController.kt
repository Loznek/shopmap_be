package com.example.shoppingList

import com.example.plugins.FirebaseUserPrincipal
import com.example.shoppingList.dto.toModel
import com.example.shoppingList.dto.CreateShoppingListRequest
import com.example.shoppingList.dto.UpdateShoppingListRequest


class ShoppingListController(
    private val shoppingListService: ShoppingListService
) {

    suspend fun getLists(
        principal: FirebaseUserPrincipal) = shoppingListService.getLists(principal)

    suspend fun getList(
        principal: FirebaseUserPrincipal,
        id: Int) = shoppingListService.getList(
            principal,
            id
        )

    suspend fun createList(
        principal: FirebaseUserPrincipal,
        request: CreateShoppingListRequest) = shoppingListService.createList(
            principal,
            request.name,
            request.items.map { it.toModel() }
        )


    suspend fun deleteList(principal: FirebaseUserPrincipal, id: Int) = shoppingListService.deleteList(principal, id)
    suspend fun updateList(
        principal: FirebaseUserPrincipal,
        request: UpdateShoppingListRequest) = shoppingListService.updateList(
            principal,
            request.id,
            request.name,
            request.items.map {
                it.toModel()
            }
        )
}