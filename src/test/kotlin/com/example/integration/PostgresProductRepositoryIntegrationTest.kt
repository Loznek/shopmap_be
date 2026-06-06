package com.example.repository

import com.example.db.mapping.DepartmentTable
import com.example.db.mapping.MapTable
import com.example.db.mapping.ProductTable
import com.example.db.mapping.StoreTable
import com.example.model.entity.*
import com.example.model.repository.*
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class ProductRepositoryIntegrationTest {

    private val storeRepository =
        PostgresStoreRepository()

    private val mapRepository =
        PostgresMapRepository()

    private val departmentRepository =
        PostgresDepartmentRepository()

    private val productRepository =
        PostgresProductRepository()

    @BeforeTest
    fun setup() {

        Database.connect(
            url = "jdbc:postgresql://localhost:5432/shopmaptest",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "123456"
        )

        transaction {

            ProductTable.deleteAll()
            DepartmentTable.deleteAll()
            MapTable.deleteAll()
            StoreTable.deleteAll()
        }
    }

    @Test
    fun `addProduct persists entity`() = runTest {

        val ctx = createContext()

        val created =
            productRepository.addProduct(
                Product(
                    articleNo = null,
                    name = "Milk",
                    size = "1L",
                    departmentId = ctx.department.id,
                    position = ProductPosition.TOP,
                    storeId = ctx.store.id!!,
                    price = 499.0
                )
            )

        assertNotNull(created.articleNo)

        assertEquals(
            "Milk",
            created.name
        )

        assertEquals(
            ProductPosition.TOP,
            created.position
        )

        assertEquals(
            499.0,
            created.price
        )
    }

    @Test
    fun `productById returns stored entity`() = runTest {

        val ctx = createContext()

        val created =
            productRepository.addProduct(
                Product(
                    name = "Milk",
                    size = "1L",
                    departmentId = ctx.department.id,
                    position = ProductPosition.TOP,
                    storeId = ctx.store.id!!,
                    price = 499.0
                )
            )

        val loaded =
            productRepository.productById(
                created.articleNo!!
            )

        assertNotNull(loaded)

        assertEquals(
            created.articleNo,
            loaded.articleNo
        )

        assertEquals(
            "Milk",
            loaded.name
        )
    }

    @Test
    fun `productsByStoreId returns only matching products`() = runTest {

        val ctx1 = createContext()
        val ctx2 = createContext()

        productRepository.addProduct(
            Product(
                name = "Milk",
                departmentId = ctx1.department.id,
                position = ProductPosition.TOP,
                storeId = ctx1.store.id!!
            )
        )

        productRepository.addProduct(
            Product(
                name = "Bread",
                departmentId = ctx2.department.id,
                position = ProductPosition.BOTTOM,
                storeId = ctx2.store.id!!
            )
        )

        val result =
            productRepository.productsByStoreId(
                ctx1.store.id!!
            )

        assertEquals(
            1,
            result.size
        )

        assertEquals(
            "Milk",
            result.first().name
        )
    }

    @Test
    fun `updateProduct updates stored values`() = runTest {

        val ctx = createContext()

        val created =
            productRepository.addProduct(
                Product(
                    name = "Milk",
                    departmentId = ctx.department.id,
                    position = ProductPosition.TOP,
                    storeId = ctx.store.id!!,
                    price = 499.0
                )
            )

        productRepository.updateProduct(
            created.copy(
                name = "Milk Updated",
                price = 599.0
            )
        )

        val loaded =
            productRepository.productById(
                created.articleNo!!
            )

        assertNotNull(loaded)

        assertEquals(
            "Milk Updated",
            loaded.name
        )

        assertEquals(
            599.0,
            loaded.price
        )
    }

    @Test
    fun `removeProductById deletes product`() = runTest {

        val ctx = createContext()

        val created =
            productRepository.addProduct(
                Product(
                    name = "Milk",
                    departmentId = ctx.department.id,
                    position = ProductPosition.TOP,
                    storeId = ctx.store.id!!
                )
            )

        val deleted =
            productRepository.removeProductById(
                created.articleNo!!
            )

        assertTrue(deleted)

        val loaded =
            productRepository.productById(
                created.articleNo!!
            )

        assertNull(loaded)
    }

    private data class TestContext(
        val store: Store,
        val map: com.example.model.entity.Map,
        val department: Department
    )

    private suspend fun createContext(): TestContext {

        val store =
            storeRepository.addStore(
                Store(
                    id = null,
                    name = "Store-${System.nanoTime()}",
                    location = "Budapest"
                )
            )

        val map =
            mapRepository.addMap(
                Map(
                    id = null,
                    storeId = store.id!!,
                    width = 500.0,
                    height = 300.0,
                    entranceX = 250.0,
                    entranceY = 0.0,
                    exitX = 250.0,
                    exitY = 300.0
                )
            )

        val department =
            departmentRepository.addDepartment(
                Department(
                    id = null,
                    mapId = map.id!!,
                    name = "Bakery",
                    width = 100.0,
                    height = 50.0,
                    startX = 10.0,
                    startY = 20.0
                )
            )

        return TestContext(
            store,
            map,
            department
        )
    }
}