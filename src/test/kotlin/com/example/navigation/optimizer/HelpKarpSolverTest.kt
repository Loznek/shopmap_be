package com.example.navigation.optimizer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HeldKarpSolverTest {

    private val solver = HeldKarpSolver()

    @Test
    fun `solveOrder returns optimal route for one destination`() {

        val distances = arrayOf(
            intArrayOf(0, 5, 100),
            intArrayOf(5, 0, 5),
            intArrayOf(100, 5, 0)
        )

        val result =
            solver.solveOrder(
                distances = distances,
                destinationCount = 1,
                endIndex = 2
            )

        assertEquals(
            listOf(0, 1, 2),
            result
        )
    }

    @Test
    fun `solveOrder visits every destination exactly once`() {

        val distances = arrayOf(
            intArrayOf(0, 1, 2, 10),
            intArrayOf(1, 0, 1, 5),
            intArrayOf(2, 1, 0, 1),
            intArrayOf(10, 5, 1, 0)
        )

        val result =
            solver.solveOrder(
                distances = distances,
                destinationCount = 2,
                endIndex = 3
            )

        assertEquals(
            4,
            result.size
        )

        assertEquals(
            setOf(0, 1, 2, 3),
            result.toSet()
        )
    }

    @Test
    fun `solveOrder starts at entrance`() {

        val distances = arrayOf(
            intArrayOf(0, 1, 2, 3),
            intArrayOf(1, 0, 1, 2),
            intArrayOf(2, 1, 0, 1),
            intArrayOf(3, 2, 1, 0)
        )

        val result =
            solver.solveOrder(
                distances,
                destinationCount = 2,
                endIndex = 3
            )

        assertEquals(
            0,
            result.first()
        )
    }

    @Test
    fun `solveOrder ends at till`() {

        val distances = arrayOf(
            intArrayOf(0, 1, 2, 3),
            intArrayOf(1, 0, 1, 2),
            intArrayOf(2, 1, 0, 1),
            intArrayOf(3, 2, 1, 0)
        )

        val result =
            solver.solveOrder(
                distances,
                destinationCount = 2,
                endIndex = 3
            )

        assertEquals(
            3,
            result.last()
        )
    }

    @Test
    fun `solveOrder chooses optimal route`() {

        val distances = arrayOf(
            intArrayOf(0, 1, 100, 100),
            intArrayOf(1, 0, 1, 100),
            intArrayOf(100, 1, 0, 1),
            intArrayOf(100, 100, 1, 0)
        )

        val result =
            solver.solveOrder(
                distances,
                destinationCount = 2,
                endIndex = 3
            )

        assertEquals(
            listOf(0, 1, 2, 3),
            result
        )
    }

    @Test
    fun `solveOrder returns empty list when destination unreachable`() {

        val inf = 1_000_000_000

        val distances = arrayOf(
            intArrayOf(0, inf, inf),
            intArrayOf(inf, 0, inf),
            intArrayOf(inf, inf, 0)
        )

        val result =
            solver.solveOrder(
                distances,
                destinationCount = 1,
                endIndex = 2
            )

        assertTrue(
            result.isEmpty()
        )
    }
}