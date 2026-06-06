package com.example.tills


import com.example.departments.TestData
import com.example.exception.ComputationException
import com.example.exception.NotFoundException
import com.example.exception.ValidationException
import com.example.model.repository.DepartmentRepository
import com.example.model.repository.MapRepository
import com.example.model.repository.TillRepository
import com.example.model.repository.WallBlockRepository
import com.example.navigation.GridBuilder
import com.example.navigation.PathFinder
import com.example.navigation.PathValidator
import io.mockk.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class TillServiceTest {

    private val tillRepository =
        mockk<TillRepository>()

    private val wallBlockRepository =
        mockk<WallBlockRepository>()

    private val departmentRepository =
        mockk<DepartmentRepository>()

    private val mapRepository =
        mockk<MapRepository>()

    private val gridBuilder =
        mockk<GridBuilder>()

    private val pathFinder =
        mockk<PathFinder>()

    private val service =
        TillService(
            tillRepository,
            wallBlockRepository,
            departmentRepository,
            mapRepository,
            gridBuilder,
            pathFinder
        )

    @Test
    fun `get throws NotFoundException when till not found`() = runTest {

        coEvery {
            tillRepository.tillById(1)
        } returns null

        assertFailsWith<NotFoundException> {
            service.get(1)
        }
    }

    @Test
    fun `get returns till`() = runTest {

        coEvery {
            tillRepository.tillById(1)
        } returns TestData.till()

        val result =
            service.get(1)

        assertEquals(
            TestData.till().id,
            result.id
        )
    }

    @Test
    fun `getByMap throws NotFoundException when map not found`() = runTest {

        coEvery {
            mapRepository.mapById(1)
        } returns null

        assertFailsWith<NotFoundException> {
            service.getByMap(1)
        }
    }

    @Test
    fun `delete throws NotFoundException when till not found`() = runTest {

        coEvery {
            tillRepository.removeTillById(1)
        } returns false

        assertFailsWith<NotFoundException> {
            service.delete(1)
        }
    }

    @Test
    fun `create throws ValidationException when till position is invalid`() = runTest {

        setupRepositories()

        assertFailsWith<ValidationException> {

            service.create(
                TestData.till().copy(
                    startX = -10.0
                )
            )
        }
    }

    @Test
    fun `create returns created till`() = runTest {

        setupRepositories()

        coEvery {
            tillRepository.addTill(any())
        } returns TestData.till()

        val result =
            service.create(
                TestData.till()
            )

        assertEquals(
            TestData.till().id,
            result.id
        )

        coVerify(exactly = 1) {
            tillRepository.addTill(any())
        }
    }

    @Test
    fun `update throws ValidationException when till position is invalid`() = runTest {

        setupRepositories()

        assertFailsWith<ValidationException> {

            service.update(
                TestData.till().copy(
                    startX = -10.0
                )
            )
        }
    }

    @Test
    fun `update throws ComputationException when till becomes unreachable`() = runTest {

        setupRepositories()

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

            service.update(
                TestData.till()
            )
        }
    }

    @Test
    fun `update returns updated till`() = runTest {

        setupRepositories()

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
            0 to 0
        )

        every {
            pathFinder.dfsPathExist(
                any(),
                any(),
                any()
            )
        } returns true

        coEvery {
            tillRepository.updateTill(any())
        } returns TestData.till()

        val result =
            service.update(
                TestData.till()
            )

        assertEquals(
            TestData.till().id,
            result.id
        )
    }

    private suspend fun setupRepositories() {

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
        } returns emptyList()
    }
}