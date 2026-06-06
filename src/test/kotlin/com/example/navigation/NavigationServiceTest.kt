package com.example.navigation


import com.example.departments.TestData
import com.example.exception.ComputationException
import com.example.model.entity.ProductPosition
import com.example.model.repository.DepartmentRepository
import com.example.model.repository.MapRepository
import com.example.model.repository.TillRepository
import com.example.model.repository.WallBlockRepository
import com.example.navigation.dto.RoutePlanningProduct
import com.example.navigation.optimizer.RouteOptimizer
import io.ktor.server.plugins.NotFoundException
import io.mockk.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class NavigationServiceTest {

    private val mapRepository = mockk<MapRepository>()
    private val departmentRepository = mockk<DepartmentRepository>()
    private val tillRepository = mockk<TillRepository>()
    private val wallBlockRepository = mockk<WallBlockRepository>()

    private val gridBuilder = mockk<GridBuilder>()
    private val pathFinder = mockk<PathFinder>()
    private val distanceMatrixBuilder = mockk<DistanceMatrixBuilder>()

    private val optimizerFactory = mockk<RouteOptimizerFactory>()
    private val optimizer = mockk<RouteOptimizer>()

    private val service =
        NavigationService(
            mapRepository,
            departmentRepository,
            tillRepository,
            wallBlockRepository,
            gridBuilder,
            pathFinder,
            distanceMatrixBuilder,
            optimizerFactory
        )

    @Test
    fun `calculateRoute throws NotFoundException when map does not exist`() = runTest {

        coEvery {
            mapRepository.mapById(any())
        } returns null

        assertFailsWith<NotFoundException> {
            service.calculateRoute(
                mapId = 1,
                products = emptyList()
            )
        }
    }

    @Test
    fun `calculateRoute throws NotFoundException when no tills exist`() = runTest {

        coEvery {
            mapRepository.mapById(any())
        } returns TestData.map()

        coEvery {
            departmentRepository.departmentsByMap(any())
        } returns emptyList()

        coEvery {
            tillRepository.tillsByMap(any())
        } returns emptyList()

        assertFailsWith<NotFoundException> {
            service.calculateRoute(
                mapId = 1,
                products = emptyList()
            )
        }
    }

    @Test
    fun `calculateRoute throws ComputationException when destination point is unreachable`() = runTest {

        val department = TestData.department()

        coEvery {
            mapRepository.mapById(any())
        } returns TestData.map()

        coEvery {
            departmentRepository.departmentsByMap(any())
        } returns listOf(department)

        coEvery {
            tillRepository.tillsByMap(any())
        } returns listOf(TestData.till())

        coEvery {
            wallBlockRepository.wallBlocksByMap(any())
        } returns emptyList()

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

        val product =
            RoutePlanningProduct(
                articleNo = 1,
                departmentId = department.id!!,
                position = ProductPosition.TOP
            )

        assertFailsWith<ComputationException> {
            service.calculateRoute(
                mapId = 1,
                products = listOf(product)
            )
        }
    }

    @Test
    fun `calculateRoute throws ComputationException when optimizer returns empty route`() = runTest {

        setupSuccessfulRepositories()

        every {
            distanceMatrixBuilder.build(any(), any())
        } returns arrayOf(
            intArrayOf(0, 10),
            intArrayOf(10, 0)
        )

        every {
            optimizerFactory.getOptimizer(any())
        } returns optimizer

        every {
            optimizer.solveOrder(
                any(),
                any(),
                any()
            )
        } returns emptyList()

        assertFailsWith<ComputationException> {
            service.calculateRoute(
                mapId = 1,
                products = emptyList()
            )
        }
    }

    @Test
    fun `calculateRoute throws ComputationException when path reconstruction fails`() = runTest {

        setupSuccessfulRepositories()

        every {
            distanceMatrixBuilder.build(any(), any())
        } returns arrayOf(
            intArrayOf(0, 10),
            intArrayOf(10, 0)
        )

        every {
            optimizerFactory.getOptimizer(any())
        } returns optimizer

        every {
            optimizer.solveOrder(
                any(),
                any(),
                any()
            )
        } returns listOf(0, 1)

        every {
            pathFinder.computeFullPath(
                any(),
                any()
            )
        } returns emptyList()

        assertFailsWith<ComputationException> {
            service.calculateRoute(
                mapId = 1,
                products = emptyList()
            )
        }
    }

    @Test
    fun `calculateRoute returns route when input is valid`() = runTest {

        setupSuccessfulRepositories()

        every {
            distanceMatrixBuilder.build(any(), any())
        } returns arrayOf(
            intArrayOf(0, 10),
            intArrayOf(10, 0)
        )

        every {
            optimizerFactory.getOptimizer(any())
        } returns optimizer

        every {
            optimizer.solveOrder(
                any(),
                any(),
                any()
            )
        } returns listOf(0, 1)

        every {
            pathFinder.computeFullPath(
                any(),
                any()
            )
        } returns listOf(
            0 to 0,
            1 to 0,
            2 to 0
        )

        val result =
            service.calculateRoute(
                mapId = 1,
                products = emptyList()
            )

        assertEquals(
            3,
            result.size
        )
    }

    private suspend fun setupSuccessfulRepositories() {

        coEvery {
            mapRepository.mapById(any())
        } returns TestData.map()

        coEvery {
            departmentRepository.departmentsByMap(any())
        } returns emptyList()

        coEvery {
            tillRepository.tillsByMap(any())
        } returns listOf(TestData.till())

        coEvery {
            wallBlockRepository.wallBlocksByMap(any())
        } returns emptyList()

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
        } returns setOf(
            0 to 0,
            1 to 0,
            2 to 0
        )
    }
}