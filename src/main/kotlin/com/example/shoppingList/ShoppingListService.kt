package com.example.shoppingList

import com.example.model.repository.UserRepository
import com.example.exception.AuthenticationException
import com.example.exception.NotFoundException
import com.example.exception.ValidationException
import com.example.plugins.FirebaseUserPrincipal

import com.example.model.entity.ShoppingList
import com.example.model.entity.ShoppingListItem
import com.example.repository.ShoppingListItemRepository
import com.example.repository.ShoppingListRepository


class ShoppingListService(
    private val shoppingListRepository: ShoppingListRepository,
    private val shoppingListItemRepository: ShoppingListItemRepository,
    private val userRepository: UserRepository
) {

    suspend fun getLists(
        principal: FirebaseUserPrincipal
    ): List<ShoppingList> {

        val user =
            userRepository.getByFirebaseUid(principal.uid)
                ?: throw AuthenticationException("User not found")

        return shoppingListRepository
            .getShoppingListsByUser(user.id)
    }

    suspend fun getList(
        principal: FirebaseUserPrincipal,
        listId: Int
    ): ShoppingList {

        val user =
            userRepository.getByFirebaseUid(principal.uid)
                ?: throw AuthenticationException("User not found")

        val shoppingList =
            shoppingListRepository.getShoppingList(listId)
                ?: throw NotFoundException(
                    "Shopping list $listId not found"
                )

        if (shoppingList.userId != user.id)
            throw AuthenticationException("Access denied")

        return shoppingList
    }

    suspend fun createList(
        principal: FirebaseUserPrincipal,
        name: String,
        items: List<ShoppingListItem>
    ): ShoppingList {

        if (name.isBlank()) {

            throw ValidationException(
                "Shopping list name cannot be empty"
            )
        }

        val user =
            userRepository.getByFirebaseUid(principal.uid)
                ?: throw AuthenticationException("User not found")

        val list =
            shoppingListRepository.addShoppingList(
                ShoppingList(
                    id = null,
                    userId = user.id,
                    name = name
                )
            )

        items.forEach {

            shoppingListItemRepository.addShoppingListItem(
                it.copy(
                    itemId = null,
                    shoppingListId = list.id!!
                )
            )
        }

        return list
    }

    suspend fun deleteList(
        principal: FirebaseUserPrincipal,
        listId: Int
    ) {

        val user =
            userRepository.getByFirebaseUid(principal.uid)
                ?: throw IllegalStateException("User not found")

        val shoppingList =
            shoppingListRepository.getShoppingList(listId)
                ?: throw NoSuchElementException()

        if (shoppingList.userId != user.id)
            throw IllegalAccessException()

        shoppingListRepository.deleteShoppingList(listId)
    }

    suspend fun updateList(
        principal: FirebaseUserPrincipal,
        listId: Int,
        name: String,
        items: List<ShoppingListItem>
    ): ShoppingList {

        if (name.isBlank()) {
            throw ValidationException(
                "Shopping list name cannot be empty"
            )
        }

        val user =
            userRepository.getByFirebaseUid(principal.uid)
                ?: throw AuthenticationException(
                    "User not found"
                )

        val existing =
            shoppingListRepository.getShoppingList(listId)
                ?: throw NotFoundException(
                    "Shopping list $listId not found"
                )

        if (existing.userId != user.id) {
            throw AuthenticationException(
                "Access denied"
            )
        }

        val updated =
            shoppingListRepository.updateShoppingList(
                existing.copy(
                    name = name
                )
            )

        shoppingListItemRepository
            .getShoppingListItems(listId)
            .forEach {
                shoppingListItemRepository
                    .deleteShoppingListItem(it.itemId!!)
            }

        items.forEach {
            shoppingListItemRepository
                .addShoppingListItem(
                    it.copy(
                        itemId = null,
                        shoppingListId = listId
                    )
                )
        }

        return updated
    }

}