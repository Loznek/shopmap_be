

import FirebaseUserPrincipal
import UserRepository

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
                ?: throw IllegalStateException("User not found")

        return shoppingListRepository
            .getShoppingListsByUser(user.id)
    }

    suspend fun getList(
        principal: FirebaseUserPrincipal,
        listId: Int
    ): ShoppingList {

        val user =
            userRepository.getByFirebaseUid(principal.uid)
                ?: throw IllegalStateException("User not found")

        val shoppingList =
            shoppingListRepository.getShoppingList(listId)
                ?: throw NoSuchElementException()

        if (shoppingList.userId != user.id)
            throw IllegalAccessException()

        return shoppingList
    }

    suspend fun createList(
        principal: FirebaseUserPrincipal,
        name: String,
        items: List<ShoppingListItem>
    ): ShoppingList {

        val user =
            userRepository.getByFirebaseUid(principal.uid)
                ?: throw IllegalStateException("User not found")

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
}