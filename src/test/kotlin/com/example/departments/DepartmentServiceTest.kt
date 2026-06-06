package com.example.departments

import com.example.departments.dto.CreateDepartmentRequest
import com.example.departments.dto.UpdateDepartmentRequest
import com.example.departments.dto.toEntity
import com.example.exception.ComputationException
import com.example.exception.NotFoundException
import com.example.exception.ValidationException
import com.example.geometry.SpatialValidator
import com.example.model.repository.*
import com.example.navigation.GridBuilder
import com.example.navigation.PathFinder
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import io.mockk.*
import kotlin.test.*

class DepartmentServiceTest {

    private val departmentRepository = mockk<DepartmentRepository>()
    private val wallBlockRepository = mockk<WallBlockRepository>()
    private val tillRepository = mockk<TillRepository>()
    private val mapRepository = mockk<MapRepository>()
    private val gridBuilder = mockk<GridBuilder>()
    private val pathFinder = mockk<PathFinder>()

    private lateinit var service: DepartmentService

    @BeforeTest
    fun setup() {

        service = DepartmentService(
            departmentRepository,
            wallBlockRepository,
            tillRepository,
            mapRepository,
            gridBuilder,
            pathFinder
        )

        mockkObject(SpatialValidator)
    }

    @AfterTest
    fun cleanup() {
        unmockkAll()
    }

    @Test
    fun `create throws ValidationException when position is invalid`() = runTest{

        // arrange

        every {
            SpatialValidator.isValidPosition(
                any(),
                any(),
                any()
            )
        } returns false

        coEvery {
            mapRepository.mapById(any())
        } returns TestData.map()

        coEvery {
            wallBlockRepository.wallBlocksByMap(any())
        } returns emptyList()

        coEvery {
            departmentRepository.departmentsByMap(any())
        } returns emptyList()

        coEvery {
            tillRepository.tillsByMap(any())
        } returns listOf(TestData.till())

        val request =
            CreateDepartmentRequest(
                mapId = 1,
                name = "Bakery",
                startX = 10.0,
                startY = 10.0,
                width = 20.0,
                height = 10.0
            )

        // act + assert

        assertFailsWith<ValidationException> {

                service.create(request)

        }
    }

    @Test
    fun `create throws NotFoundException when map does not exist`() = runTest {

        coEvery {
            mapRepository.mapById(any())
        } returns null

        val request = CreateDepartmentRequest(
            mapId = 1,
            name = "Bakery",
            startX = 10.0,
            startY = 10.0,
            width = 20.0,
            height = 10.0
        )

        assertFailsWith<NotFoundException> {
            service.create(request)
        }
    }

    @Test
    fun `create throws ComputationException when department blocks access to tills`() = runTest {

        every {
            SpatialValidator.isValidPosition(any(), any(), any())
        } returns true

        coEvery {
            mapRepository.mapById(any())
        } returns TestData.map()

        coEvery {
            wallBlockRepository.wallBlocksByMap(any())
        } returns emptyList()

        coEvery {
            departmentRepository.departmentsByMap(any())
        } returns emptyList()

        coEvery {
            tillRepository.tillsByMap(any())
        } returns listOf(TestData.till())

        every {
            gridBuilder.buildWalkablePoints(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns emptySet()

        every {
            pathFinder.dfsPathExist(
                any(),
                any(),
                any()
            )
        } returns false

        val request = CreateDepartmentRequest(
            mapId = 1,
            name = "Bakery",
            startX = 10.0,
            startY = 10.0,
            width = 20.0,
            height = 10.0
        )

        assertFailsWith<ComputationException> {
            service.create(request)
        }
    }

    @Test
    fun `create adds department when request is valid`() = runTest {

        every {
            SpatialValidator.isValidPosition(any(), any(), any())
        } returns true

        coEvery {
            mapRepository.mapById(any())
        } returns TestData.map()

        coEvery {
            wallBlockRepository.wallBlocksByMap(any())
        } returns emptyList()

        coEvery {
            departmentRepository.departmentsByMap(any())
        } returns emptyList()

        coEvery {
            tillRepository.tillsByMap(any())
        } returns listOf(TestData.till())

        every {
            gridBuilder.buildWalkablePoints(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns emptySet()

        every {
            pathFinder.dfsPathExist(
                any(),
                any(),
                any()
            )
        } returns true

        val department =
            CreateDepartmentRequest(
                mapId = 1,
                name = "Bakery",
                startX = 10.0,
                startY = 10.0,
                width = 20.0,
                height = 10.0
            ).toEntity()

        coEvery {
            departmentRepository.addDepartment(any())
        } returns department.copy(id = 1)

        service.create(
            CreateDepartmentRequest(
                mapId = 1,
                name = "Bakery",
                startX = 10.0,
                startY = 10.0,
                width = 20.0,
                height = 10.0
            )
        )

        coVerify(exactly = 1) {
            departmentRepository.addDepartment(any())
        }
    }

    @Test
    fun `get returns department when exists`() = runTest {

        val department = TestData.department()

        coEvery {
            departmentRepository.departmentById(1)
        } returns department

        val result = service.get(1)

        assertEquals(department, result)
    }

    @Test
    fun `get throws NotFoundException when department does not exist`() = runTest {

        coEvery {
            departmentRepository.departmentById(1)
        } returns null

        assertFailsWith<NotFoundException> {
            service.get(1)
        }
    }

    @Test
    fun `delete removes existing department`() = runTest {

        coEvery {
            departmentRepository.removeDepartmentById(1)
        } returns true

        service.delete(1)

        coVerify(exactly = 1) {
            departmentRepository.removeDepartmentById(1)
        }
    }

    @Test
    fun `delete throws NotFoundException when department does not exist`() = runTest {

        coEvery {
            departmentRepository.removeDepartmentById(1)
        } returns false

        assertFailsWith<NotFoundException> {
            service.delete(1)
        }
    }

    @Test
    fun `update throws NotFoundException when department does not exist`() = runTest {

        val request = UpdateDepartmentRequest(
            id = 1,
            mapId = 1,
            name = "Updated",
            startX = 10.0,
            startY = 10.0,
            width = 20.0,
            height = 10.0
        )

        coEvery {
            departmentRepository.departmentById(1)
        } returns null

        assertFailsWith<NotFoundException> {
            service.update(request)
        }
    }

    @Test
    fun `update skips validation when position unchanged`() = runTest {

        val existing = TestData.department()

        val request = UpdateDepartmentRequest(
            id = existing.id!!,
            mapId = existing.mapId,
            name = "New Name",
            startX = existing.startX,
            startY = existing.startY,
            width = existing.width,
            height = existing.height
        )

        coEvery {
            departmentRepository.departmentById(existing.id!!)
        } returns existing

        coEvery {
            departmentRepository.updateDepartment(any())
        } returns request.toEntity()

        service.update(request)

        coVerify(exactly = 1) {
            departmentRepository.updateDepartment(any())
        }

        verify(exactly = 0) {
            gridBuilder.buildWalkablePoints(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        }
    }

    @Test
    fun `update throws ComputationException when department blocks access to tills`() = runTest {

        val existing = TestData.department()

        val request = UpdateDepartmentRequest(
            id = existing.id!!,
            mapId = existing.mapId,
            name = existing.name,
            startX = 50.0,
            startY = 50.0,
            width = 50.0,
            height = 50.0
        )

        coEvery {
            departmentRepository.departmentById(existing.id!!)
        } returns existing

        coEvery {
            mapRepository.mapById(any())
        } returns TestData.map()

        coEvery {
            wallBlockRepository.wallBlocksByMap(any())
        } returns emptyList()

        coEvery {
            departmentRepository.departmentsByMap(any())
        } returns emptyList()

        coEvery {
            tillRepository.tillsByMap(any())
        } returns listOf(TestData.till())

        every {
            SpatialValidator.isValidPosition(
                any(),
                any(),
                any()
            )
        } returns true

        every {
            gridBuilder.buildWalkablePoints(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns emptySet()

        every {
            pathFinder.dfsPathExist(
                any(),
                any(),
                any()
            )
        } returns false

        assertFailsWith<ComputationException> {
            service.update(request)
        }
    }

    @Test
    fun `update succeeds when department position is valid`() = runTest {

        val existing = TestData.department()

        val request = UpdateDepartmentRequest(
            id = existing.id!!,
            mapId = existing.mapId,
            name = existing.name,
            startX = 50.0,
            startY = 50.0,
            width = 50.0,
            height = 50.0
        )

        coEvery {
            departmentRepository.departmentById(existing.id!!)
        } returns existing

        coEvery {
            mapRepository.mapById(any())
        } returns TestData.map()

        coEvery {
            wallBlockRepository.wallBlocksByMap(any())
        } returns emptyList()

        coEvery {
            departmentRepository.departmentsByMap(any())
        } returns emptyList()

        coEvery {
            tillRepository.tillsByMap(any())
        } returns listOf(TestData.till())

        every {
            SpatialValidator.isValidPosition(
                any(),
                any(),
                any()
            )
        } returns true

        every {
            gridBuilder.buildWalkablePoints(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns emptySet()

        every {
            pathFinder.dfsPathExist(
                any(),
                any(),
                any()
            )
        } returns true

        coEvery {
            departmentRepository.updateDepartment(any())
        } returns request.toEntity()

        service.update(request)

        coVerify(exactly = 1) {
            departmentRepository.updateDepartment(any())
        }
    }
}