package com.example.wallblocks


import com.example.departments.TestData
import com.example.exception.ComputationException
import com.example.exception.NotFoundException
import com.example.exception.ValidationException
import com.example.model.entity.WallBlock
import com.example.model.repository.DepartmentRepository
import com.example.model.repository.MapRepository
import com.example.model.repository.TillRepository
import com.example.model.repository.WallBlockRepository
import com.example.navigation.GridBuilder
import com.example.navigation.PathValidator
import io.mockk.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class WallBlockServiceTest {

    private val wallBlockRepository =
        mockk<WallBlockRepository>()

    private val departmentRepository =
        mockk<DepartmentRepository>()

    private val tillRepository =
        mockk<TillRepository>()

    private val mapRepository =
        mockk<MapRepository>()

    private val gridBuilder =
        mockk<GridBuilder>()

    private val pathValidator =
        mockk<PathValidator>()

    private val service =
        WallBlockService(
            wallBlockRepository,
            departmentRepository,
            tillRepository,
            mapRepository,
            gridBuilder,
            pathValidator
        )

    @Test
    fun `get throws NotFoundException when wall block not found`() = runTest {

        coEvery {
            wallBlockRepository.wallBlockById(1)
        } returns null

        assertFailsWith<NotFoundException> {
            service.get(1)
        }
    }

    @Test
    fun `get returns wall block`() = runTest {

        coEvery {
            wallBlockRepository.wallBlockById(1)
        } returns TestData.wallBlock()

        val result = service.get(1)

        assertEquals(
            TestData.wallBlock().id,
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
    fun `delete throws NotFoundException when wall block not found`() = runTest {

        coEvery {
            wallBlockRepository.removeWallBlockById(1)
        } returns false

        assertFailsWith<NotFoundException> {
            service.delete(1)
        }
    }

    @Test
    fun `create throws ValidationException when position is invalid`() = runTest {

        setupRepositories()

        assertFailsWith<ValidationException> {

            service.create(
                TestData.wallBlock().copy(
                    startX = -10.0
                )
            )
        }
    }

    @Test
    fun `create throws ComputationException when wall blocks access to tills`() = runTest {

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
            pathValidator.pathExists(
                any(),
                any(),
                any()
            )
        } returns false

        assertFailsWith<ComputationException> {

            service.create(
                TestData.wallBlock()
            )
        }
    }

    @Test
    fun `create returns created wall block`() = runTest {

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
            pathValidator.pathExists(
                any(),
                any(),
                any()
            )
        } returns true

        coEvery {
            wallBlockRepository.addWallBlock(any())
        } returns TestData.wallBlock()

        val result =
            service.create(
                TestData.wallBlock()
            )

        assertEquals(
            TestData.wallBlock().id,
            result.id
        )
    }

    @Test
    fun `update throws ValidationException when position is invalid`() = runTest {

        setupRepositories()

        assertFailsWith<ValidationException> {

            service.update(
                TestData.wallBlock().copy(
                    startX = -10.0
                )
            )
        }
    }

    @Test
    fun `update throws ComputationException when wall blocks access to tills`() = runTest {

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
            pathValidator.pathExists(
                any(),
                any(),
                any()
            )
        } returns false

        assertFailsWith<ComputationException> {

            service.update(
                TestData.wallBlock()
            )
        }
    }

    @Test
    fun `update returns updated wall block`() = runTest {

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
            pathValidator.pathExists(
                any(),
                any(),
                any()
            )
        } returns true

        coEvery {
            wallBlockRepository.updateWallBlock(any())
        } returns TestData.wallBlock()

        val result =
            service.update(
                TestData.wallBlock()
            )

        assertEquals(
            TestData.wallBlock().id,
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
        } returns listOf(
            TestData.till()
        )
    }
}