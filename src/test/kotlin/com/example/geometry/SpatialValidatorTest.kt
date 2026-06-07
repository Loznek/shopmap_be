package com.example.geometry

import com.example.model.entity.Map
import com.example.model.entity.Rectangle
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SpatialValidatorTest {

    private val map =
        Map(
            id = 1,
            storeId = 1,
            width = 100.0,
            height = 100.0,
            entranceX = 0.0,
            entranceY = 0.0,
            exitX = 100.0,
            exitY = 100.0
        )

    @Test
    fun `isValidPosition returns true for rectangle inside map without intersections`() {

        val rect =
            Rectangle(
                x = 10.0,
                y = 10.0,
                width = 20.0,
                height = 20.0
            )

        val result =
            SpatialValidator.isValidPosition(
                rect,
                map,
                emptyList()
            )

        assertTrue(result)
    }

    @Test
    fun `isValidPosition returns false when rectangle starts outside map`() {

        val rect =
            Rectangle(
                x = -1.0,
                y = 10.0,
                width = 20.0,
                height = 20.0
            )

        val result =
            SpatialValidator.isValidPosition(
                rect,
                map,
                emptyList()
            )

        assertFalse(result)
    }

    @Test
    fun `isValidPosition returns false when rectangle exceeds map width`() {

        val rect =
            Rectangle(
                x = 90.0,
                y = 10.0,
                width = 20.0,
                height = 20.0
            )

        val result =
            SpatialValidator.isValidPosition(
                rect,
                map,
                emptyList()
            )

        assertFalse(result)
    }

    @Test
    fun `isValidPosition returns false when rectangle exceeds map height`() {

        val rect =
            Rectangle(
                x = 10.0,
                y = 90.0,
                width = 20.0,
                height = 20.0
            )

        val result =
            SpatialValidator.isValidPosition(
                rect,
                map,
                emptyList()
            )

        assertFalse(result)
    }

    @Test
    fun `isValidPosition returns false when width is zero`() {

        val rect =
            Rectangle(
                x = 10.0,
                y = 10.0,
                width = 0.0,
                height = 20.0
            )

        val result =
            SpatialValidator.isValidPosition(
                rect,
                map,
                emptyList()
            )

        assertFalse(result)
    }

    @Test
    fun `isValidPosition returns false when rectangle intersects obstacle`() {

        val rect =
            Rectangle(
                x = 20.0,
                y = 20.0,
                width = 20.0,
                height = 20.0
            )

        val obstacle =
            Rectangle(
                x = 30.0,
                y = 30.0,
                width = 20.0,
                height = 20.0
            )

        val result =
            SpatialValidator.isValidPosition(
                rect,
                map,
                listOf(obstacle)
            )

        assertFalse(result)
    }

    @Test
    fun `isValidPosition returns true when rectangle only touches obstacle edge`() {

        val rect =
            Rectangle(
                x = 20.0,
                y = 20.0,
                width = 20.0,
                height = 20.0
            )

        val obstacle =
            Rectangle(
                x = 40.0,
                y = 20.0,
                width = 20.0,
                height = 20.0
            )

        val result =
            SpatialValidator.isValidPosition(
                rect,
                map,
                listOf(obstacle)
            )

        assertTrue(result)
    }

    @Test
    fun `canResizeMap returns true when all objects fit`() {

        val objects =
            listOf(
                Rectangle(10.0, 10.0, 20.0, 20.0),
                Rectangle(50.0, 50.0, 10.0, 10.0)
            )

        val result =
            SpatialValidator.canResizeMap(
                newWidth = 100.0,
                newHeight = 100.0,
                objects = objects
            )

        assertTrue(result)
    }

    @Test
    fun `canResizeMap returns false when object exceeds new width`() {

        val objects =
            listOf(
                Rectangle(90.0, 10.0, 20.0, 20.0)
            )

        val result =
            SpatialValidator.canResizeMap(
                newWidth = 100.0,
                newHeight = 100.0,
                objects = objects
            )

        assertFalse(result)
    }

    @Test
    fun `canResizeMap returns false when object exceeds new height`() {

        val objects =
            listOf(
                Rectangle(10.0, 90.0, 20.0, 20.0)
            )

        val result =
            SpatialValidator.canResizeMap(
                newWidth = 100.0,
                newHeight = 100.0,
                objects = objects
            )

        assertFalse(result)
    }

    @Test
    fun `canResizeMap returns false for negative dimensions`() {

        val result =
            SpatialValidator.canResizeMap(
                newWidth = -1.0,
                newHeight = 100.0,
                objects = emptyList()
            )

        assertFalse(result)
    }
}