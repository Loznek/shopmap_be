package com.example.navigation.optimizer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TwoOptSolverTest {

    private val solver = TwoOptSolver()

    private fun routeCost(
        route: List<Int>,
        distances: Array<IntArray>
    ): Int {

        var cost = 0

        for (i in 0 until route.lastIndex) {
            cost += distances[route[i]][route[i + 1]]
        }

        return cost
    }

    @Test
    fun `optimize keeps valid route`() {

        val distances = arrayOf(
            intArrayOf(0, 10, 15, 20),
            intArrayOf(10, 0, 35, 25),
            intArrayOf(15, 35, 0, 30),
            intArrayOf(20, 25, 30, 0)
        )

        val route =
            mutableListOf(
                0,
                1,
                2,
                3
            )

        val optimized =
            solver.optimize(
                route,
                distances
            )

        assertEquals(
            route.first(),
            optimized.first()
        )

        assertEquals(
            route.last(),
            optimized.last()
        )

        assertEquals(
            route.toSet(),
            optimized.toSet()
        )
    }

    @Test
    fun `optimize never worsens route cost`() {

        val distances = arrayOf(
            intArrayOf(0, 1, 100, 1),
            intArrayOf(1, 0, 1, 100),
            intArrayOf(100, 1, 0, 1),
            intArrayOf(1, 100, 1, 0)
        )

        val route =
            mutableListOf(
                0,
                2,
                1,
                3
            )

        val originalCost =
            routeCost(
                route,
                distances
            )

        val optimized =
            solver.optimize(
                route,
                distances
            )

        val optimizedCost =
            routeCost(
                optimized,
                distances
            )

        assertTrue(
            optimizedCost <= originalCost
        )
    }

    @Test
    fun `optimize returns same route when already optimal`() {

        val distances = arrayOf(
            intArrayOf(0, 1, 2),
            intArrayOf(1, 0, 1),
            intArrayOf(2, 1, 0)
        )

        val route =
            mutableListOf(
                0,
                1,
                2
            )

        val optimized =
            solver.optimize(
                route,
                distances
            )

        assertEquals(
            route,
            optimized
        )
    }
}