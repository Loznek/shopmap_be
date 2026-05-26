package com.example.navigation.optimizer


class NearestNeighborSolver (
    private val twoOptSolver: TwoOptSolver
): RouteOptimizer {

    override fun solveOrder(
        distances: Array<IntArray>,
        destinationCount: Int,
        endIndex: Int
    ): List<Int> {

        val visited =
            BooleanArray(endIndex + 1)

        val route =
            mutableListOf<Int>()

        var current = 0

        route.add(current)

        visited[current] = true

        repeat(destinationCount) {

            var best = -1
            var bestDistance = Int.MAX_VALUE

            for (candidate in 1..destinationCount) {

                if (visited[candidate]) {
                    continue
                }

                val distance =
                    distances[current][candidate]

                if (distance < bestDistance) {
                    bestDistance = distance
                    best = candidate
                }
            }

            visited[best] = true

            route.add(best)

            current = best
        }

        route.add(endIndex)

        return twoOptSolver.optimize(
            route.toMutableList(),
            distances
        )
    }
}