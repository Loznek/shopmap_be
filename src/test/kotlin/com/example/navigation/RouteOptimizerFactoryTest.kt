package com.example.navigation

import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

class DistanceMatrixBuilderTest {

    private val pathFinder = mockk<PathFinder>()

    private val builder =
        DistanceMatrixBuilder(
            pathFinder
        )

    @Test
    fun `build creates distance matrix`() {

        val walkable =
            setOf(
                0 to 0,
                1 to 0,
                2 to 0
            )

        val points =
            listOf(
                0 to 0,
                1 to 0,
                2 to 0
            )

        every {
            pathFinder.bfsDistanceMap(
                walkable,
                0 to 0
            )
        } returns mapOf(
            (0 to 0) to 0,
            (1 to 0) to 1,
            (2 to 0) to 2
        )

        every {
            pathFinder.bfsDistanceMap(
                walkable,
                1 to 0
            )
        } returns mapOf(
            (0 to 0) to 1,
            (1 to 0) to 0,
            (2 to 0) to 1
        )

        every {
            pathFinder.bfsDistanceMap(
                walkable,
                2 to 0
            )
        } returns mapOf(
            (0 to 0) to 2,
            (1 to 0) to 1,
            (2 to 0) to 0
        )

        val matrix =
            builder.build(
                walkable,
                points
            )

        assertEquals(0, matrix[0][0])
        assertEquals(1, matrix[0][1])
        assertEquals(2, matrix[0][2])

        assertEquals(1, matrix[1][0])
        assertEquals(0, matrix[1][1])
        assertEquals(1, matrix[1][2])

        assertEquals(2, matrix[2][0])
        assertEquals(1, matrix[2][1])
        assertEquals(0, matrix[2][2])
    }

    @Test
    fun `build stores INF for unreachable points`() {

        val walkable =
            setOf(
                0 to 0
            )

        val points =
            listOf(
                0 to 0,
                5 to 5
            )

        every {
            pathFinder.bfsDistanceMap(
                any(),
                any()
            )
        } returns mapOf(
            (0 to 0) to 0
        )

        val matrix =
            builder.build(
                walkable,
                points
            )

        assertEquals(
            1_000_000_000,
            matrix[0][1]
        )
    }
}