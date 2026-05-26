package com.example.navigation.optimizer


class TwoOptSolver {

    fun optimize(
        route: MutableList<Int>,
        distances: Array<IntArray>
    ): List<Int> {

        var improved = true

        while (improved) {

            improved = false

            for (i in 1 until route.size - 2) {

                for (j in i + 1 until route.size - 1) {

                    val candidate =
                        route.toMutableList()

                    candidate.subList(i, j + 1)
                        .reverse()

                    if (
                        routeCost(candidate, distances)
                        <
                        routeCost(route, distances)
                    ) {

                        route.clear()
                        route.addAll(candidate)

                        improved = true
                    }
                }
            }
        }

        return route
    }

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
}