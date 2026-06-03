package com.example.navigation

import kotlin.test.Test
import kotlin.test.assertEquals

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
}