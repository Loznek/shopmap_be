package com.example.navigation.optimizer

import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class NearestNeighborSolverTest {

    private val twoOptSolver =
        mockk<TwoOptSolver>()

    private val solver =
        NearestNeighborSolver(
            twoOptSolver
        )

    @Test
    fun `solveOrder visits every destination exactly once`() {

        val distances = arrayOf(
            intArrayOf(0, 1, 2, 3),
            intArrayOf(1, 0, 1, 2),
            intArrayOf(2, 1, 0, 1),
            intArrayOf(3, 2, 1, 0)
        )

        every {
            twoOptSolver.optimize(
                any(),
                any()
            )
        } answers {
            firstArg()
        }

        val result =
            solver.solveOrder(
                distances = distances,
                destinationCount = 2,
                endIndex = 3
            )

        assertEquals(
            listOf(
                0,
                1,
                2,
                3
            ),
            result
        )
    }

    @Test
    fun `solveOrder starts at entrance and ends at till`() {

        val distances = arrayOf(
            intArrayOf(0, 5, 10),
            intArrayOf(5, 0, 1),
            intArrayOf(10, 1, 0)
        )

        every {
            twoOptSolver.optimize(
                any(),
                any()
            )
        } answers {
            firstArg()
        }

        val result =
            solver.solveOrder(
                distances = distances,
                destinationCount = 1,
                endIndex = 2
            )

        assertEquals(
            0,
            result.first()
        )

        assertEquals(
            2,
            result.last()
        )
    }

    @Test
    fun `solveOrder chooses nearest destination first`() {

        val distances = arrayOf(
            intArrayOf(0, 2, 10, 20),
            intArrayOf(2, 0, 5, 10),
            intArrayOf(10, 5, 0, 2),
            intArrayOf(20, 10, 2, 0)
        )

        every {
            twoOptSolver.optimize(
                any(),
                any()
            )
        } answers {
            firstArg()
        }

        val result =
            solver.solveOrder(
                distances = distances,
                destinationCount = 2,
                endIndex = 3
            )

        assertEquals(
            1,
            result[1]
        )
    }

    @Test
    fun `solveOrder throws when no destination reachable`() {

        val distances = arrayOf(
            intArrayOf(0, Int.MAX_VALUE),
            intArrayOf(Int.MAX_VALUE, 0)
        )

        every {
            twoOptSolver.optimize(
                any(),
                any()
            )
        } answers {
            firstArg()
        }

        assertFailsWith<IllegalArgumentException> {

            solver.solveOrder(
                distances = distances,
                destinationCount = 1,
                endIndex = 1
            )
        }
    }
}