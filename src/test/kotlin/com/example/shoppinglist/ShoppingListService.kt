package com.example.shoppingList

import com.example.model.repository.UserRepository
import com.example.departments.TestData
import com.example.exception.AuthenticationException
import com.example.exception.NotFoundException
import com.example.exception.ValidationException
import com.example.model.entity.ShoppingList
import com.example.model.entity.ShoppingListItem
import com.example.plugins.FirebaseUserPrincipal
import com.example.repository.ShoppingListItemRepository
import com.example.repository.ShoppingListRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class ShoppingListServiceTest {

    private val shoppingListRepository =
        mockk<ShoppingListRepository>()

    private val shoppingListItemRepository =
        mockk<ShoppingListItemRepository>()

    private val userRepository =
        mockk<UserRepository>()

    private val service =
        ShoppingListService(
            shoppingListRepository,
            shoppingListItemRepository,
            userRepository
        )

    private val principal =
        FirebaseUserPrincipal(
            uid = "firebase-user",
            email = "test@test.com",
            displayName = "Test User"
        )

    @Test
    fun `getLists throws AuthenticationException when user not found`() = runTest {

        coEvery {
            userRepository.getByFirebaseUid(any())
        } returns null

        assertFailsWith<AuthenticationException> {
            service.getLists(principal)
        }
    }

    @Test
    fun `getLists returns shopping lists`() = runTest {

        setupUser()

        coEvery {
            shoppingListRepository.getShoppingListsByUser(1)
        } returns listOf(
            shoppingList()
        )

        val result =
            service.getLists(principal)

        assertEquals(
            1,
            result.size
        )
    }

    @Test
    fun `getList throws NotFoundException when list does not exist`() = runTest {

        setupUser()

        coEvery {
            shoppingListRepository.getShoppingList(1)
        } returns null

        assertFailsWith<NotFoundException> {
            service.getList(
                principal,
                1
            )
        }
    }

    @Test
    fun `getList throws AuthenticationException when list belongs to another user`() = runTest {

        setupUser()

        coEvery {
            shoppingListRepository.getShoppingList(1)
        } returns shoppingList().copy(
            userId = 99
        )

        assertFailsWith<AuthenticationException> {
            service.getList(
                principal,
                1
            )
        }
    }

    @Test
    fun `getList returns shopping list`() = runTest {

        setupUser()

        coEvery {
            shoppingListRepository.getShoppingList(1)
        } returns shoppingList()

        val result =
            service.getList(
                principal,
                1
            )

        assertEquals(
            1,
            result.id
        )
    }

    @Test
    fun `createList throws ValidationException when name is blank`() = runTest {

        assertFailsWith<ValidationException> {

            service.createList(
                principal,
                "",
                emptyList()
            )
        }
    }

    @Test
    fun `createList creates shopping list and items`() = runTest {

        setupUser()

        coEvery {
            shoppingListRepository.addShoppingList(any())
        } returns shoppingList()

        coEvery {
            shoppingListItemRepository.addShoppingListItem(any())
        } returns ShoppingListItem(
                itemId = 1,
                shoppingListId = 1,
                shoppingItemName = "Milk",
                attributes = "1L"
            )

        val items =
            listOf(
                ShoppingListItem(
                    itemId = null,
                    shoppingListId = 1,
                    shoppingItemName = "Milk",
                    attributes = "1L"
                )
            )

        val result =
            service.createList(
                principal,
                "Weekly shopping",
                items
            )

        assertEquals(
            1,
            result.id
        )

        coVerify(exactly = 1) {
            shoppingListRepository.addShoppingList(any())
        }

        coVerify(exactly = 1) {
            shoppingListItemRepository.addShoppingListItem(any())
        }
    }

    @Test
    fun `deleteList throws when list belongs to another user`() = runTest {

        setupUser()

        coEvery {
            shoppingListRepository.getShoppingList(1)
        } returns shoppingList().copy(
            userId = 99
        )

        assertFailsWith<IllegalAccessException> {

            service.deleteList(
                principal,
                1
            )
        }
    }

    @Test
    fun `deleteList deletes shopping list`() = runTest {

        setupUser()

        coEvery {
            shoppingListRepository.getShoppingList(1)
        } returns shoppingList()

        coEvery {
            shoppingListRepository.deleteShoppingList(1)
        } just Runs

        service.deleteList(
            principal,
            1
        )

        coVerify(exactly = 1) {
            shoppingListRepository.deleteShoppingList(1)
        }
    }

    private suspend fun setupUser() {

        coEvery {
            userRepository.getByFirebaseUid(any())
        } returns TestData.user()
    }

    private fun shoppingList() =
        ShoppingList(
            id = 1,
            userId = 1,
            name = "Weekly shopping"
        )
}