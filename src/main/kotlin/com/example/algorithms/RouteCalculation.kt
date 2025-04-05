package com.example.algorithms

import com.example.model.entity.Department
import com.example.model.entity.Map
import com.example.model.entity.Till
import com.example.model.entity.WallBlock
import kotlinx.coroutines.*
import kotlin.system.measureNanoTime

data class GridCell(val x: Int, val y: Int)


class RouteCalculation {



    fun heldKarpShortestPathParallel(
        walkablePoints: Set<Pair<Int, Int>>,
        destinations: List<Pair<Int, Int>>, // Only intermediate points
        start: Pair<Int, Int>,
        end: Pair<Int, Int>
    ): List<Pair<Int, Int>> {
        val points = listOf(start) + destinations + listOf(end)
        val n = points.size
        val startIndex = 0
        val endIndex = points.size - 1

        // Step 1: Precompute distances between all points using BFS
        val distances = Array(n) { IntArray(n) { Int.MAX_VALUE } }
        for (i in points.indices) {
            for (j in points.indices) {
                if (i != j) {
                    distances[i][j] = bfsShortestPath(walkablePoints, points[i], points[j]).size
                }
            }
        }

        // Step 2: Solve TSP using Held-Karp with fixed start and end
        val intermediateIndices = (1 until points.size - 1).toList() // Indices of intermediate points
        val dp = Array(1 shl intermediateIndices.size) { IntArray(n) { Int.MAX_VALUE } }
        val parent = Array(1 shl intermediateIndices.size) { IntArray(n) { -1 } }
        dp[0][startIndex] = 0

        runBlocking {
            val numThreads = Runtime.getRuntime().availableProcessors()
            val chunkSize = (1 shl intermediateIndices.size) / numThreads
            val jobs = mutableListOf<Deferred<Unit>>()

            for (chunk in 0 until numThreads) {
                val startMask = chunk * chunkSize
                val endMask = minOf((chunk + 1) * chunkSize, 1 shl intermediateIndices.size)

                jobs.add(async {
                    for (mask in startMask until endMask) {
                        for (u in intermediateIndices) {
                            if (mask and (1 shl (u - 1)) != 0) { // If `u` is in the subset
                                val prevMask = mask and (1 shl (u - 1)).inv()
                                for (v in intermediateIndices + startIndex) { // Consider neighbors
                                    if (dp[prevMask][v] < Int.MAX_VALUE && distances[v][u] < Int.MAX_VALUE) {
                                        val newDist = dp[prevMask][v] + distances[v][u]
                                        synchronized(dp) { // Ensure thread-safe updates
                                            if (newDist < dp[mask][u]) {
                                                dp[mask][u] = newDist
                                                parent[mask][u] = v
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
            }

            // Wait for all jobs to complete
            jobs.awaitAll()
        }

        // Add the fixed ending point
        var minCost = Int.MAX_VALUE
        var lastPoint = -1
        val fullMask = (1 shl intermediateIndices.size) - 1
        for (u in intermediateIndices) {
            val cost = dp[fullMask][u] + distances[u][endIndex]
            if (cost < minCost) {
                minCost = cost
                lastPoint = u
            }
        }

        // Step 3: Reconstruct the path
        val path = mutableListOf<Pair<Int, Int>>()
        var mask = fullMask
        var current = lastPoint

        while (current != -1) {
            path.add(points[current])
            val prev = parent[mask][current]
            if (prev != -1) {
                mask = mask and (1 shl (current - 1)).inv()
            }
            current = prev
        }

        path.reverse()
        path.add(end) // Add the fixed ending point last
        return computeFullPathWithDetails(walkablePoints, listOf(start) + path.drop(1))
    }





    fun calculateRoutes(
        map: Map,
        tills: List<Till>,
        wallBlocks: List<WallBlock>,
        allDepartments: List<Department>,
        destinationDepartmentIds: List<Int>
    ): MutableList<Pair<Int, Int>> {
        val finalPath = mutableListOf<Pair<Int, Int>>()
        val irrelevantDepartments = allDepartments.filter { it.id !in destinationDepartmentIds }
        val relevantDepartments = allDepartments.filter { it.id in destinationDepartmentIds }
        val destinationPoints = relevantDepartments.map { department ->
            Triple(department.id, department.startX + department.width-1, department.startY)
        }.toMutableList()

        val points = mutableListOf<Pair<Int, Int>>()

        for (x in 1 until (map.width + 1).toInt()) {
            for (y in 1 until (map.height + 1).toInt()) {
                // Check if the point is inside any wall block
                val isInWall = wallBlocks.any { wall ->
                    x in wall.startX.toInt() until (wall.startX + wall.width).toInt() &&
                            y in wall.startY.toInt() until (wall.startY + wall.height).toInt()
                }
                // Add to the list if not in a wall block
                val isInIrrelevantDep = allDepartments.any { dep ->
                    x in dep.startX.toInt() until (dep.startX + dep.width).toInt() &&
                            y in dep.startY.toInt() until (dep.startY + dep.height).toInt()
                }

                if (!isInWall && !isInIrrelevantDep) {
                    points.add(x to y)
                }
            }
        }
        for (point in destinationPoints) {
            points.add(point.second.toInt() to point.third.toInt())
        }
        points.add(tills[0].startX.toInt() to tills[0].startY.toInt())
        var shortestPath = mutableListOf<Pair<Int, Int>>()
        var shortestPathLength = Int.MAX_VALUE
        var shortestPathDestinaionId=-1
        for (destpoint in destinationPoints) {
            val path = bfsShortestPath(
                points.toSet(),
                Pair(map.entranceX.toInt(), map.entranceY.toInt()),
                Pair(destpoint.second.toInt(), destpoint.third.toInt())
            )
            if (path.size < shortestPathLength) {
                shortestPath = path.toMutableList()
                shortestPathLength = path.size
                shortestPathDestinaionId= destpoint.first!!
            }

        }

        finalPath.addAll(shortestPath.dropLast(1))
        var currentIndex = destinationPoints.indexOfFirst { it.first == shortestPathDestinaionId }
        var currentPoint = destinationPoints[currentIndex].second.toInt() to destinationPoints[currentIndex].third.toInt()
        destinationPoints.removeAt(currentIndex)

        while (destinationPoints.isNotEmpty()) {
            var shortestPath = mutableListOf<Pair<Int, Int>>()
            var shortestPathLength = Int.MAX_VALUE
            var closestDestinationIndex = -1

            // Find the closest destination
            for ((index, destPoint) in destinationPoints.withIndex()) {
                val path = bfsShortestPath(
                    points.toSet(),
                    currentPoint,
                    destPoint.second.toInt() to destPoint.third.toInt()
                )
                if (path.isNotEmpty() && path.size < shortestPathLength) {
                    shortestPath = path.toMutableList()
                    shortestPathLength = path.size
                    closestDestinationIndex = index
                }
            }

            // If no more paths can be found, break
            if (closestDestinationIndex == -1) break

            // Add the shortest path to the final path
            finalPath.addAll(shortestPath.dropLast(1))

            // Update current point to the newly reached destination
            currentPoint = destinationPoints[closestDestinationIndex].second.toInt() to destinationPoints[closestDestinationIndex].third.toInt()

            // Remove the visited destination from the list
            destinationPoints.removeAt(closestDestinationIndex)
        }

        val path = bfsShortestPath(
            points.toSet(),
            currentPoint,
            tills[0].startX.toInt() to tills[0].startY.toInt()
        )
        finalPath.addAll(path)

        return finalPath

    }


    /*
    fun bfsShortestPah(
        start: Pair<Int, Int>,
        walkablePoints: MutableList<Pair<Int, Int>>,
        destination: Pair<Int, Int>
    ): List<Pair<Int, Int>> {
        val directions = listOf(
            0 to 1, // Up
            1 to 0, // Right
            0 to -1, // Down
            -1 to 0 // Left
        )

        val queue = ArrayDeque<Pair<Pair<Int, Int>, List<Pair<Int, Int>>>>()
        queue.add(start to listOf(start)) // Start point with initial path

        val visited = mutableSetOf<Pair<Int, Int>>()
        visited.add(start)

        while (queue.isNotEmpty()) {
            val (current, path) = queue.removeFirst()

            // If current point is the destination, return the path
            if (current == destination) {
                return path
            }

            // Explore neighbors
            for ((dx, dy) in directions) {
                val neighbor = current.first + dx to current.second + dy

                // Add to queue if it's walkable and not yet visited
                if (neighbor in walkablePoints && neighbor !in visited) {
                    visited.add(neighbor)
                    queue.add(neighbor to path + neighbor)
                }
            }
        }

        // If no path is found
        return emptyList()
    }*/

    fun calculateShortestRoutes( map: Map,
                                 tills: List<Till>,
                                 wallBlocks: List<WallBlock>,
                                 allDepartments: List<Department>,
                                 destinationDepartmentIds: List<Int>): List<Pair<Int, Int>> {
        val irrelevantDepartments = allDepartments.filter { it.id !in destinationDepartmentIds }
        val relevantDepartments = allDepartments.filter { it.id in destinationDepartmentIds }
        val destinationPoints = relevantDepartments.map { department ->
            Triple(department.id, department.startX + department.width-1, department.startY)
        }.toMutableList()


        val walkablePoints = mutableListOf<Pair<Int, Int>>()

        for (x in 1 until (map.width + 1).toInt()) {
            for (y in 1 until (map.height + 1).toInt()) {
                // Check if the point is inside any wall block
                val isInWall = wallBlocks.any { wall ->
                    x in wall.startX.toInt() until (wall.startX + wall.width).toInt() &&
                            y in wall.startY.toInt() until (wall.startY + wall.height).toInt()
                }
                // Add to the list if not in a wall block
                val isInIrrelevantDep = allDepartments.any { dep ->
                    x in dep.startX.toInt() until (dep.startX + dep.width).toInt() &&
                            y in dep.startY.toInt() until (dep.startY + dep.height).toInt()
                }

                if (!isInWall && !isInIrrelevantDep) {
                    walkablePoints.add(x to y)
                }
            }
        }
        for (point in destinationPoints) {
            walkablePoints.add(point.second.toInt() to point.third.toInt())
        }
        walkablePoints.add(tills[0].startX.toInt() to tills[0].startY.toInt())


        //return bellmanFordShortestPath(points, Pair(map.entranceX.toInt(), map.entranceY.toInt()), destinationPoints.map { it.second.toInt() to it.third.toInt() })
        val start= Pair(map.entranceX.toInt(), map.entranceY.toInt())
        val fixedDestination = tills[0].startX.toInt() to tills[0].startY.toInt()
        var path = emptyList<Pair<Int, Int>>()
        val executionTimeNs = measureNanoTime {
            path= heldKarpShortestPath(walkablePoints.toSet(), destinationPoints.map { it.second.toInt() to it.third.toInt() }, start, fixedDestination)
        }

        // Convert time to milliseconds
        val executionTimeMs = executionTimeNs / 1_000_000.0
        println("Execution Time: $executionTimeMs ms")



        return path

    }




    fun heldKarpShortestPath(
        walkablePoints: Set<Pair<Int, Int>>,
        destinations: List<Pair<Int, Int>>, // Only intermediate points
        start: Pair<Int, Int>,
        end: Pair<Int, Int>
    ): List<Pair<Int, Int>> {
        val points = listOf(start) + destinations + listOf(end)
        val n = points.size
        val startIndex = 0
        val endIndex = points.size - 1

        // Step 1: Precompute distances between all points using BFS
        val distances = Array(n) { IntArray(n) { Int.MAX_VALUE } }
        for (i in points.indices) {
            for (j in points.indices) {
                if (i != j) {
                    distances[i][j] = bfsShortestPath(walkablePoints, points[i], points[j]).size
                }
            }
        }

        // Step 2: Solve TSP using Held-Karp with fixed start and end
        val intermediateIndices = (1 until points.size - 1).toList() // Indices of intermediate points
        val dp = Array(1 shl intermediateIndices.size) { IntArray(n) { Int.MAX_VALUE } }
        val parent = Array(1 shl intermediateIndices.size) { IntArray(n) { -1 } }
        dp[0][startIndex] = 0

        for (mask in 0 until (1 shl intermediateIndices.size)) {
            for (u in intermediateIndices) {
                if (mask and (1 shl (u - 1)) != 0) { // If `u` is in the subset
                    val prevMask = mask and (1 shl (u - 1)).inv()
                    for (v in intermediateIndices + startIndex) { // Consider neighbors
                        if (dp[prevMask][v] < Int.MAX_VALUE && distances[v][u] < Int.MAX_VALUE) {
                            val newDist = dp[prevMask][v] + distances[v][u]
                            if (newDist < dp[mask][u]) {
                                dp[mask][u] = newDist
                                parent[mask][u] = v
                            }
                        }
                    }
                }
            }
        }

        // Add the fixed ending point
        var minCost = Int.MAX_VALUE
        var lastPoint = -1
        val fullMask = (1 shl intermediateIndices.size) - 1
        for (u in intermediateIndices) {
            val cost = dp[fullMask][u] + distances[u][endIndex]
            if (cost < minCost) {
                minCost = cost
                lastPoint = u
            }
        }

        // Step 3: Reconstruct the path
        val path = mutableListOf<Pair<Int, Int>>()
        var mask = fullMask
        var current = lastPoint

        while (current != -1) {
            path.add(points[current])
            val prev = parent[mask][current]
            if (prev != -1) {
                mask = mask and (1 shl (current - 1)).inv()
            }
            current = prev
        }

        path.reverse()
        path.add(end) // Add the fixed ending point last
        return computeFullPathWithDetails(walkablePoints, listOf(start) + path.drop(1))
    //return listOf(start) + path.drop(1) // Ensure starting point is first
    }

    /*
    fun bfsDistance(
        walkablePoints: Set<Pair<Int, Int>>,
        start: Pair<Int, Int>,
        end: Pair<Int, Int>
    ): Int {
        val directions = listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0) // Four possible moves
        val queue = ArrayDeque<Pair<Pair<Int, Int>, Int>>() // Pair of (point, distance)
        queue.add(start to 0)
        val visited = mutableSetOf(start)

        while (queue.isNotEmpty()) {
            val (current, distance) = queue.removeFirst()

            if (current == end) {
                return distance
            }

            for ((dx, dy) in directions) {
                val neighbor = current.first + dx to current.second + dy
                if (neighbor in walkablePoints && neighbor !in visited) {
                    visited.add(neighbor)
                    queue.add(neighbor to distance + 1)
                }
            }
        }

        return Int.MAX_VALUE // Return a very high value if no path exists
    }*/

    fun computeFullPathWithDetails(
        walkablePoints: Set<Pair<Int, Int>>,
        orderedStops: List<Pair<Int, Int>>
    ): List<Pair<Int, Int>> {
        val fullPath = mutableListOf<Pair<Int, Int>>()

        for (i in 0 until orderedStops.size - 1) {
            val start = orderedStops[i]
            val end = orderedStops[i + 1]
            val pathBetween = bfsShortestPath(walkablePoints, start, end)
            if (fullPath.isEmpty()) {
                fullPath.addAll(pathBetween)
            } else {
                // Avoid duplicating the last point of the previous segment
                fullPath.addAll(pathBetween.drop(1))
            }
        }

        return fullPath
    }

    fun bfsShortestPath(
        walkablePoints: Set<Pair<Int, Int>>,
        start: Pair<Int, Int>,
        end: Pair<Int, Int>
    ): List<Pair<Int, Int>> {
        val directions = listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0) // Four possible moves
        val queue = ArrayDeque<Pair<Pair<Int, Int>, List<Pair<Int, Int>>>>()
        queue.add(start to listOf(start))
        val visited = mutableSetOf(start)

        while (queue.isNotEmpty()) {
            val (current, path) = queue.removeFirst()

            if (current == end) {
                return path
            }

            for ((dx, dy) in directions) {
                val neighbor = current.first + dx to current.second + dy
                if (neighbor in walkablePoints && neighbor !in visited) {
                    visited.add(neighbor)
                    queue.add(neighbor to path + neighbor)
                }
            }
        }

        return emptyList() // No path found
    }

    fun checkPathExistence(
        startX: Double,
        startY: Double,
        width: Double,
        height: Double,
        map: Map,
        tills: List<Till>,
        wallBlocks: List<WallBlock>,
        departments: List<Department>,
        depId: Int,
        wallId: Int
    ): Boolean {



        val points =  mutableListOf<Pair<Int, Int>>()

        for (x in 1 until (map.width + 1).toInt()) {
            for (y in 1 until (map.height + 1).toInt()) {
                // Check if the point is inside any wall block
                val isInWall = wallBlocks.filter { it.id != wallId }.any { wall ->
                    x in wall.startX.toInt() until (wall.startX + wall.width).toInt() &&
                            y in wall.startY.toInt() until (wall.startY + wall.height).toInt()
                }
                // Add to the list if not in a wall block
                val isInIrrelevantDep = departments.filter { it.id != depId }.any { dep ->
                    x in dep.startX.toInt() until (dep.startX + dep.width).toInt() &&
                            y in dep.startY.toInt() until (dep.startY + dep.height).toInt()
                }

                val isInNewParameters = x in startX.toInt() until (startX + width).toInt() &&
                        y in startY.toInt() until (startY + height).toInt()

                if (!isInWall && !isInIrrelevantDep && !isInNewParameters) {
                    points.add(x to y)
                }
            }
        }

        points.add(tills[0].startX.toInt() to tills[0].startY.toInt())
        return bfsShortestPath(
            points.toSet(),
            Pair(map.entranceX.toInt(), map.entranceY.toInt()),
            Pair(tills[0].startX.toInt(), tills[0].startY.toInt())
        ).isNotEmpty()



    }


}
