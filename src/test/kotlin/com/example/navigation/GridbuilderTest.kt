package com.example.navigation
import com.example.departments.TestData
import com.example.model.entity.Department
import com.example.model.entity.Rectangle
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class GridBuilderTest {

    private val gridBuilder = GridBuilder()

    @Test
    fun `buildWalkablePoints excludes department area`() {

        val department = Department(
            id = 1,
            mapId = 1,
            name = "Bakery",
            startX = 10.0,
            startY = 10.0,
            width = 10.0,
            height = 10.0
        )

        val result =
            gridBuilder.buildWalkablePoints(
                map = TestData.map(),
                wallBlocks = emptyList(),
                departments = listOf(department),
                tills = listOf(TestData.till()),
                newRect = null,
                excludeWallId = -1,
                excludeDepartmentId = -1
            )

        assertFalse(20 to 20 in result)
    }

    @Test
    fun `buildWalkablePoints keeps free points walkable`() {

        val result =
            gridBuilder.buildWalkablePoints(
                map = TestData.map(),
                wallBlocks = emptyList(),
                departments = emptyList(),
                tills = listOf(TestData.till()),
                newRect = null,
                excludeWallId = -1,
                excludeDepartmentId = -1
            )

        assertTrue(10 to 10 in result)
    }

    @Test
    fun `buildWalkablePoints excludes new rectangle`() {

        val result =
            gridBuilder.buildWalkablePoints(
                map = TestData.map(),
                wallBlocks = emptyList(),
                departments = emptyList(),
                tills = listOf(TestData.till()),
                newRect = Rectangle(
                    x = 10.0,
                    y = 10.0,
                    width = 10.0,
                    height = 10.0
                ),
                excludeWallId = -1,
                excludeDepartmentId = -1
            )

        assertFalse(20 to 20 in result)
    }

    @Test
    fun `buildWalkablePoints ignores excluded department`() {

        val department = Department(
            id = 1,
            mapId = 1,
            name = "Bakery",
            startX = 10.0,
            startY = 10.0,
            width = 10.0,
            height = 10.0
        )

        val result =
            gridBuilder.buildWalkablePoints(
                map = TestData.map(),
                wallBlocks = emptyList(),
                departments = listOf(department),
                tills = listOf(TestData.till()),
                newRect = null,
                excludeWallId = -1,
                excludeDepartmentId = 1
            )

        assertTrue(20 to 20 in result)
    }

    @Test
    fun `buildWalkablePoints throws when entrance blocked`() {

        val blockingDepartment = Department(
            id = 1,
            mapId = 1,
            name = "Blocking",
            startX = 240.0,
            startY = 0.0,
            width = 20.0,
            height = 20.0
        )

        assertFailsWith<IllegalStateException> {

            gridBuilder.buildWalkablePoints(
                map = TestData.map(),
                wallBlocks = emptyList(),
                departments = listOf(blockingDepartment),
                tills = listOf(TestData.till()),
                newRect = null,
                excludeWallId = -1,
                excludeDepartmentId = -1
            )
        }
    }

    @Test
    fun `buildWalkablePoints throws when till blocked`() {

        val blockingDepartment = Department(
            id = 1,
            mapId = 1,
            name = "Blocking",
            startX = 90.0,
            startY = 240.0,
            width = 320.0,
            height = 40.0
        )

        assertFailsWith<IllegalStateException> {

            gridBuilder.buildWalkablePoints(
                map = TestData.map(),
                wallBlocks = emptyList(),
                departments = listOf(blockingDepartment),
                tills = listOf(TestData.till()),
                newRect = null,
                excludeWallId = -1,
                excludeDepartmentId = -1
            )
        }
    }
}