package com.example.maps


import com.example.departments.TestData
import com.example.exception.NotFoundException
import com.example.exception.ValidationException
import com.example.maps.dto.Box
import com.example.maps.dto.ProcessImageRequest
import com.example.maps.dto.ProcessImageResponse
import com.example.model.repository.DepartmentRepository
import com.example.model.repository.MapRepository
import com.example.model.repository.TillRepository
import com.example.model.repository.WallBlockRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class MapServiceTest {

    private val mapRepository =
        mockk<MapRepository>()

    private val wallBlockRepository =
        mockk<WallBlockRepository>()

    private val departmentRepository =
        mockk<DepartmentRepository>()

    private val tillRepository =
        mockk<TillRepository>()

    private val pythonMapProcessorClient =
        mockk<PythonMapProcessorClient>()

    private val service =
        MapService(
            mapRepository,
            wallBlockRepository,
            departmentRepository,
            tillRepository,
            pythonMapProcessorClient
        )

    @Test
    fun `getById throws NotFoundException when map does not exist`() = runTest {

        coEvery {
            mapRepository.mapById(1)
        } returns null

        assertFailsWith<NotFoundException> {
            service.getById(1)
        }
    }

    @Test
    fun `create throws ValidationException when width is invalid`() = runTest {

        assertFailsWith<ValidationException> {

            service.create(
                TestData.map().copy(
                    width = 0.0
                )
            )
        }
    }

    @Test
    fun `create throws ValidationException when height is invalid`() = runTest {

        assertFailsWith<ValidationException> {

            service.create(
                TestData.map().copy(
                    height = -1.0
                )
            )
        }
    }

    @Test
    fun `delete throws NotFoundException when map does not exist`() = runTest {

        coEvery {
            mapRepository.removeMap(1)
        } returns false

        assertFailsWith<NotFoundException> {
            service.delete(1)
        }
    }

    @Test
    fun `update throws ValidationException when resize would invalidate objects`() = runTest {

        coEvery {
            wallBlockRepository.wallBlocksByMap(any())
        } returns listOf(
            TestData.wallBlock()
        )

        coEvery {
            departmentRepository.departmentsByMap(any())
        } returns emptyList()

        coEvery {
            tillRepository.tillsByMap(any())
        } returns emptyList()

        assertFailsWith<ValidationException> {

            service.update(
                TestData.map().copy(
                    width = 10.0,
                    height = 10.0
                )
            )
        }
    }

    @Test
    fun `processImage throws NotFoundException when no boxes detected`() = runTest {

        coEvery {
            pythonMapProcessorClient.processImage(
                any(),
                any(),
                any()
            )
        } returns ProcessImageResponse(
            boxes = emptyList()
        )

        assertFailsWith<NotFoundException> {

            service.processImage(
                imageBytes = byteArrayOf(1, 2, 3),
                request = ProcessImageRequest(
                    mapWidth = 500.0,
                    mapHeight = 300.0,
                    mapId = 1
                )
            )
        }
    }

    @Test
    fun `processImage maps python response to departments`() = runTest {

        coEvery {
            pythonMapProcessorClient.processImage(
                any(),
                any(),
                any()
            )
        } returns ProcessImageResponse(
            boxes = listOf(
                Box(
                    startX = 10.0,
                    startY = 20.0,
                    width = 30.0,
                    height = 40.0,
                    name = "Bakery"
                )
            )
        )

        val result =
            service.processImage(
                imageBytes = byteArrayOf(1, 2, 3),
                request = ProcessImageRequest(
                    mapWidth = 500.0,
                    mapHeight = 300.0,
                    mapId = 1
                )
            )

        assertEquals(
            1,
            result.size
        )

        assertEquals(
            "Bakery",
            result[0].name
        )

        assertEquals(
            1,
            result[0].mapId
        )

        assertEquals(
            10.0,
            result[0].startX
        )

        assertEquals(
            20.0,
            result[0].startY
        )

        assertEquals(
            30.0,
            result[0].width
        )

        assertEquals(
            40.0,
            result[0].height
        )
    }
}