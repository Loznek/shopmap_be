package com.example.repository

import com.example.db.mapping.DepartmentTable
import com.example.db.mapping.MapTable
import com.example.db.mapping.StoreTable
import com.example.model.entity.Department
import com.example.model.entity.Map
import com.example.model.entity.Store
import com.example.model.repository.PostgresDepartmentRepository
import com.example.model.repository.PostgresMapRepository
import com.example.model.repository.PostgresStoreRepository
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class DepartmentRepositoryIntegrationTest {

    private val departmentRepository =
        PostgresDepartmentRepository()

    private val mapRepository =
        PostgresMapRepository()

    private val storeRepository =
        PostgresStoreRepository()

    @BeforeTest
    fun setup() {

        Database.connect(
            url = "jdbc:postgresql://localhost:5432/shopmaptest",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "123456"
        )

        transaction {

            DepartmentTable.deleteAll()
            MapTable.deleteAll()
            StoreTable.deleteAll()
        }
    }

    @Test
    fun `addDepartment persists entity`() = runTest {

        val store = createStore()

        val map =
            createMap(
                store.id!!
            )

        val created =
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

        assertNotNull(
            created.id
        )

        assertEquals(
            "Bakery",
            created.name
        )
    }

    @Test
    fun `departmentById returns stored entity`() = runTest {

        val store = createStore()

        val map =
            createMap(
                store.id!!
            )

        val created =
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

        val loaded =
            departmentRepository.departmentById(
                created.id!!
            )

        assertNotNull(
            loaded
        )

        assertEquals(
            created.id,
            loaded.id
        )

        assertEquals(
            "Bakery",
            loaded.name
        )
    }

    @Test
    fun `departmentsByMap returns only departments belonging to map`() = runTest {

        val store =
            createStore()

        val store2 =
            createStore()

        val map1 =
            createMap(
                store.id!!
            )

        val map2 =
            createMap(
                store2.id!!
            )

        departmentRepository.addDepartment(
            Department(
                id = null,
                mapId = map1.id!!,
                name = "Bakery",
                width = 100.0,
                height = 50.0,
                startX = 10.0,
                startY = 20.0
            )
        )

        departmentRepository.addDepartment(
            Department(
                id = null,
                mapId = map2.id!!,
                name = "Drinks",
                width = 100.0,
                height = 50.0,
                startX = 50.0,
                startY = 60.0
            )
        )

        val departments =
            departmentRepository.departmentsByMap(
                map1.id!!
            )

        assertEquals(
            1,
            departments.size
        )

        assertEquals(
            "Bakery",
            departments.first().name
        )
    }

    @Test
    fun `updateDepartment updates stored values`() = runTest {

        val store =
            createStore()

        val map =
            createMap(
                store.id!!
            )

        val created =
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

        departmentRepository.updateDepartment(
            created.copy(
                name = "Updated Bakery",
                width = 200.0
            )
        )

        val loaded =
            departmentRepository.departmentById(
                created.id!!
            )

        assertNotNull(
            loaded
        )

        assertEquals(
            "Updated Bakery",
            loaded.name
        )

        assertEquals(
            200.0,
            loaded.width
        )
    }

    @Test
    fun `removeDepartmentById deletes department`() = runTest {

        val store =
            createStore()

        val map =
            createMap(
                store.id!!
            )

        val created =
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

        val deleted =
            departmentRepository.removeDepartmentById(
                created.id!!
            )

        assertTrue(
            deleted
        )

        val loaded =
            departmentRepository.departmentById(
                created.id!!
            )

        assertNull(
            loaded
        )
    }

    private var storeCounter = 0

    private suspend fun createStore(): Store {

        storeCounter++

        return storeRepository.addStore(
            Store(
                id = null,
                name = "Test Store $storeCounter",
                location = "Budapest"
            )
        )
    }

    private suspend fun createMap(
        storeId: Int
    ): Map {

        return mapRepository.addMap(
            Map(
                id = null,
                storeId = storeId,
                width = 500.0,
                height = 300.0,
                entranceX = 250.0,
                entranceY = 0.0,
                exitX = 250.0,
                exitY = 300.0
            )
        )
    }
}