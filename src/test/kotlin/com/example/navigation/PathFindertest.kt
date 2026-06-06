package com.example.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PathFinderTest {

    private val pathFinder = PathFinder()

    @Test
    fun `should find path between two points`() {

        val walkablePoints =
            setOf(
                1 to 1,
                2 to 1,
                3 to 1
            )

        val path =
            pathFinder.bfsShortestPath(
                walkablePoints,
                1 to 1,
                3 to 1
            )

        assertEquals(
            listOf(
                1 to 1,
                2 to 1,
                3 to 1
            ),
            path
        )
    }

    @Test
    fun `dfs finds path`() {
        val walkable = setOf(
            0 to 0,
            1 to 0,
            2 to 0
        )

        val result =
            pathFinder.dfsPathExist(
                walkable,
                0 to 0,
                2 to 0
            )

        assertTrue(result)
    }

    @Test
    fun `bfsDistanceMap calculates distances`() {

        val walkable = setOf(
            0 to 0,
            1 to 0,
            2 to 0
        )

        val result =
            pathFinder.bfsDistanceMap(
                walkable,
                0 to 0
            )

        assertEquals(0, result[0 to 0])
        assertEquals(1, result[1 to 0])
        assertEquals(2, result[2 to 0])
    }

}