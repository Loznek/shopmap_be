package com.example.navigation.optimizer

import com.example.navigation.DistanceMatrixBuilder
import com.example.navigation.PathFinder

/**
 * Solves the Traveling Salesman Problem (TSP) with:
 * - fixed start point (store entrance)
 * - fixed end point (till / checkout)
 * - intermediate destination points (departments or shelves)
 *
 * This class contains the Held-Karp dynamic programming algorithm that was
 * previously implemented in RouteCalculation. It delegates:
 * - shortest path computation to PathFinder
 * - full route reconstruction to PathFinder.computeFullPath()
 *
 * NOTE:
 * Keep your old RouteCalculation.kt file for reference until everything is
 * migrated and tested. This class is the production-ready replacement for
 * heldKarpShortestPath().
 */
class HeldKarpSolver(
): RouteOptimizer

{

    /**
     * Calculates the optimal route:
     * entrance -> all destinations (best order) -> till
     */
    /*
    fun solve(
        walkablePoints: Set<Pair<Int, Int>>,
        destinations: List<Pair<Int, Int>>,
        start: Pair<Int, Int>,
        end: Pair<Int, Int>
    ): List<Pair<Int, Int>> {

        // No destinations -> go directly from entrance to till
        if (destinations.isEmpty()) {
            return pathFinder.bfsShortestPath(
                walkablePoints = walkablePoints,
                start = start,
                end = end
            )
        }

        val points = listOf(start) + destinations + listOf(end)
        val startIndex = 0
        val endIndex = points.lastIndex
        val destinationCount = destinations.size

        // --------------------------------------------------------------------
        // Step 1: Precompute shortest path distances between all important points
        // --------------------------------------------------------------------



        val distances =
            distanceMatrixBuilder.build(
                walkablePoints = walkablePoints,
                points = points
            )

        // --------------------------------------------------------------------
        // Step 2: Held-Karp Dynamic Programming
        //
        // Destination indices inside "points":
        //   points[1] .. points[destinationCount]
        //
        // DP mask uses 0-based bits corresponding to destinations:
        //   bit 0 -> points[1]
        //   bit 1 -> points[2]
        //   ...
        // --------------------------------------------------------------------
        val subsetCount = 1 shl destinationCount

        // dp[mask][u] = minimal cost to start at entrance and finish at point u
        // after visiting destinations encoded in mask.
        val dp = Array(subsetCount) { IntArray(points.size) { Int.MAX_VALUE } }

        // parent[mask][u] = previous point index before u
        val parent = Array(subsetCount) { IntArray(points.size) { -1 } }

        // Starting state: visited nothing, located at start
        dp[0][startIndex] = 0

        for (mask in 0 until subsetCount) {
            for (destIdx in 0 until destinationCount) {
                val u = destIdx + 1 // actual index in points

                // Destination u must be included in the current subset
                if ((mask and (1 shl destIdx)) == 0) continue

                val previousMask = mask and (1 shl destIdx).inv()

                // Previous point can be:
                // - start
                // - any other destination
                for (v in 0..destinationCount) {
                    if (dp[previousMask][v] == Int.MAX_VALUE) continue
                    if (distances[v][u] == Int.MAX_VALUE) continue

                    val newCost = dp[previousMask][v] + distances[v][u]

                    if (newCost < dp[mask][u]) {
                        dp[mask][u] = newCost
                        parent[mask][u] = v
                    }
                }
            }
        }

        // --------------------------------------------------------------------
        // Step 3: Connect the last destination to the fixed end point (till)
        // --------------------------------------------------------------------
        val fullMask = subsetCount - 1

        var bestCost = Int.MAX_VALUE
        var lastPoint = -1

        for (destIdx in 0 until destinationCount) {
            val u = destIdx + 1

            if (dp[fullMask][u] == Int.MAX_VALUE) continue
            if (distances[u][endIndex] == Int.MAX_VALUE) continue

            val totalCost = dp[fullMask][u] + distances[u][endIndex]

            if (totalCost < bestCost) {
                bestCost = totalCost
                lastPoint = u
            }
        }

        // No feasible route
        if (lastPoint == -1) {
            return emptyList()
        }

        // --------------------------------------------------------------------
        // Step 4: Reconstruct optimal order of stops
        // --------------------------------------------------------------------
        val orderedStops = mutableListOf<Pair<Int, Int>>()

        var currentMask = fullMask
        var current = lastPoint

        // Add fixed end point first (will reverse later)
        orderedStops.add(points[endIndex])

        while (current != -1) {
            orderedStops.add(points[current])

            val previous = parent[currentMask][current]

            if (current in 1..destinationCount) {
                val bit = current - 1
                currentMask = currentMask and (1 shl bit).inv()
            }

            current = previous
        }

        // Reverse to obtain:
        // start -> destinations -> end
        orderedStops.reverse()

        // Ensure end is present (in case reconstruction did not include it)
        if (orderedStops.last() != points[endIndex]) {
            orderedStops.add(points[endIndex])
        }

        // --------------------------------------------------------------------
        // Step 5: Expand stop sequence into the full cell-by-cell route
        // --------------------------------------------------------------------
        return pathFinder.computeFullPath(
            walkablePoints = walkablePoints,
            stops = orderedStops
        )
    }



           val distances = Array(points.size) { IntArray(points.size) { Int.MAX_VALUE } }

           for (i in points.indices) {
               for (j in points.indices) {
                   if (i == j) continue

                   val path = pathFinder.bfsShortestPath(
                       walkablePoints = walkablePoints,
                       start = points[i],
                       end = points[j]
                   )

                   if (path.isNotEmpty()) {
                       // Number of edges, not number of vertices
                       distances[i][j] = path.size - 1
                   }
               }
           }*/


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
                    Int.MAX_VALUE
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

                // Destination u must be included in the current subset
                if ((mask and (1 shl destIdx)) == 0) continue

                val previousMask = mask and (1 shl destIdx).inv()

                // Previous point can be:
                // - start
                // - any other destination
                for (v in 0..destinationCount) {
                    if (dp[previousMask][v] == Int.MAX_VALUE) continue
                    if (distances[v][u] == Int.MAX_VALUE) continue

                    val newCost = dp[previousMask][v] + distances[v][u]

                    if (newCost < dp[mask][u]) {
                        dp[mask][u] = newCost
                        parent[mask][u] = v
                    }
                }
            }
        }

        // --------------------------------------------------------------------
        // Step 3: Connect the last destination to the fixed end point (till)
        // --------------------------------------------------------------------
        val fullMask = subsetCount - 1

        var bestCost = Int.MAX_VALUE
        var lastPoint = -1

        for (destIdx in 0 until destinationCount) {
            val u = destIdx + 1

            if (dp[fullMask][u] == Int.MAX_VALUE) continue
            if (distances[u][endIndex] == Int.MAX_VALUE) continue

            val totalCost = dp[fullMask][u] + distances[u][endIndex]

            if (totalCost < bestCost) {
                bestCost = totalCost
                lastPoint = u
            }
        }

        // No feasible route
        if (lastPoint == -1) {
            return emptyList()
        }

        // --------------------------------------------------------------------
        // Step 4: Reconstruct optimal order of stops
        // --------------------------------------------------------------------
        val orderedStops = mutableListOf<Int>()

        var currentMask = fullMask
        var current = lastPoint

        // Add fixed end point first (will reverse later)
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

        // Reverse to obtain:
        // start -> destinations -> end
        orderedStops.reverse()

        // Ensure end is present (in case reconstruction did not include it)
        if (orderedStops.last() != endIndex) {
            orderedStops.add(endIndex)
        }

        return orderedStops
    }
}