package com.example.navigation.optimizer

import com.example.navigation.DistanceMatrixBuilder
import com.example.navigation.PathFinder

class HeldKarpSolver(
): RouteOptimizer

{

    private val INF = 1_000_000_000

    override fun solveOrder(
        distances: Array<IntArray>,
        destinationCount: Int,
        endIndex: Int
    ): List<Int> {

        val startIndex = 0

        val subsetCount =
            1 shl destinationCount

        val pointCount =
            distances.size

        val dp =
            Array(subsetCount) {
                IntArray(pointCount) {
                    INF
                }
            }

        val parent =
            Array(subsetCount) {
                IntArray(pointCount) {
                    -1
                }
            }

        dp[0][startIndex] = 0

        for (mask in 0 until subsetCount) {
            for (destIdx in 0 until destinationCount) {
                val u = destIdx + 1 // actual index in points

                if ((mask and (1 shl destIdx)) == 0) continue

                val previousMask = mask and (1 shl destIdx).inv()
                for (v in 0..destinationCount) {
                    if (dp[previousMask][v] == INF) continue
                    if (distances[v][u] == INF) continue

                    val newCost = dp[previousMask][v] + distances[v][u]

                    if (newCost < dp[mask][u]) {
                        dp[mask][u] = newCost
                        parent[mask][u] = v
                    }
                }
            }
        }
        val fullMask = subsetCount - 1

        var bestCost = INF
        var lastPoint = -1

        for (destIdx in 0 until destinationCount) {
            val u = destIdx + 1

            if (dp[fullMask][u] == INF) continue
            if (distances[u][endIndex] == INF) continue

            val totalCost = dp[fullMask][u] + distances[u][endIndex]

            if (totalCost < bestCost) {
                bestCost = totalCost
                lastPoint = u
            }
        }

        if (lastPoint == -1) {
            return emptyList()
        }
        val orderedStops = mutableListOf<Int>()
        var currentMask = fullMask
        var current = lastPoint

        orderedStops.add(endIndex)

        while (current != -1) {
            orderedStops.add(current)

            val previous = parent[currentMask][current]

            if (current in 1..destinationCount) {
                val bit = current - 1
                currentMask = currentMask and (1 shl bit).inv()
            }

            current = previous
        }
        orderedStops.reverse()

         if (orderedStops.last() != endIndex) {
            orderedStops.add(endIndex)
        }
        return orderedStops
    }
}