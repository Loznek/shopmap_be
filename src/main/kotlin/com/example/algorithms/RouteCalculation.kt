package com.example.algorithms

import com.example.model.entity.Department
import com.example.model.entity.Map
import com.example.model.entity.Till
import com.example.model.entity.WallBlock

class RouteCalculation {


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
                Pair(map.entranceX.toInt(), map.entranceY.toInt()),
                points,
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
                    currentPoint,
                    points,
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
            currentPoint,
            points,
            tills[0].startX.toInt() to tills[0].startY.toInt()
        )
        finalPath.addAll(path)

        return finalPath

    }

    fun bfsShortestPath(
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
    }


}
