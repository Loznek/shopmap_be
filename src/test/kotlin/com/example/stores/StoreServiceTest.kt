package com.example.stores


import com.example.exception.NotFoundException
import com.example.exception.ValidationException
import com.example.model.entity.Product
import com.example.model.entity.ShoppingListItem
import com.example.model.entity.Store
import com.example.model.repository.*
import com.example.repository.ShoppingListItemRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class StoreServiceTest {

    private val storeRepository = mockk<StoreRepository>()
    private val mapRepository = mockk<MapRepository>()
    private val departmentRepository = mockk<DepartmentRepository>()
    private val wallBlockRepository = mockk<WallBlockRepository>()
    private val tillRepository = mockk<TillRepository>()
    private val shoppingListItemRepository = mockk<ShoppingListItemRepository>()
    private val productRepository = mockk<ProductRepository>()

    private val service =
        StoreService(
            storeRepository,
            mapRepository,
            departmentRepository,
            wallBlockRepository,
            tillRepository,
            shoppingListItemRepository,
            productRepository
        )

    @Test
    fun `getById throws when store not found`() = runTest {

        coEvery {
            storeRepository.storeById(1)
        } returns null

        assertFailsWith<NotFoundException> {
            service.getById(1)
        }
    }

    @Test
    fun `create throws when store name is blank`() = runTest {

        assertFailsWith<ValidationException> {

            service.create(
                Store(
                    id = null,
                    name = "",
                    location = null
                )
            )
        }
    }

    @Test
    fun `delete throws when store not found`() = runTest {

        coEvery {
            storeRepository.removeStore(1)
        } returns false

        assertFailsWith<NotFoundException> {
            service.delete(1)
        }
    }

    @Test
    fun `update throws when store not found`() = runTest {

        val store =
            Store(
                id = 1,
                name = "Aldi",
                location = "Budapest"
            )

        coEvery {
            storeRepository.storeById(1)
        } returns null

        assertFailsWith<NotFoundException> {
            service.update(store)
        }
    }

    @Test
    fun `getProductMatches matches products ignoring case`() = runTest {

        coEvery {
            shoppingListItemRepository.getShoppingListItems(1)
        } returns listOf(
            ShoppingListItem(
                itemId = 1,
                shoppingListId = 1,
                shoppingItemName = "milk",
                attributes = ""
            )
        )

        coEvery {
            productRepository.productsByStoreId(1)
        } returns listOf(
            Product(
                articleNo = 1,
                name = "Milk",
                storeId = 1
            )
        )

        val result =
            service.getProductMatches(
                storeId = 1,
                shoppingListId = 1
            )

        assertEquals(
            1,
            result.matches.size
        )
    }

    @Test
    fun `getStoreDetails returns empty collections when map missing`() = runTest {

        val store =
            Store(
                id = 1,
                name = "Aldi",
                location = "Budapest"
            )

        coEvery {
            storeRepository.storeById(1)
        } returns store

        coEvery {
            mapRepository.mapsByStoreId(1)
        } returns emptyList()

        val result =
            service.getStoreDetails(1)

        assertNull(
            result.map
        )

        assertTrue(
            result.departments.isEmpty()
        )

        assertTrue(
            result.wallBlocks.isEmpty()
        )

        assertTrue(
            result.tills.isEmpty()
        )
    }
}