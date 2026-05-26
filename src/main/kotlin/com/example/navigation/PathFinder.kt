package com.example.navigation

class PathFinder {

    fun bfsShortestPath(
        walkablePoints: Set<Pair<Int, Int>>,
        start: Pair<Int, Int>,
        end: Pair<Int, Int>
    ): List<Pair<Int, Int>> {
        val directions = listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0)
        val queue = ArrayDeque<Pair<Pair<Int, Int>, List<Pair<Int, Int>>>>()
        queue.add(start to listOf(start))
        val visited = mutableSetOf(start)

        while (queue.isNotEmpty()) {
            val (current, path) = queue.removeFirst()

            if (current == end) return path

            for ((dx, dy) in directions) {
                val neighbor = current.first + dx to current.second + dy
                if (neighbor in walkablePoints && neighbor !in visited) {
                    visited.add(neighbor)
                    queue.add(neighbor to path + neighbor)
                }
            }
        }

        return emptyList()
    }

    fun computeFullPath(
        walkablePoints: Set<Pair<Int, Int>>,
        stops: List<Pair<Int, Int>>
    ): List<Pair<Int, Int>> {
        val fullPath = mutableListOf<Pair<Int, Int>>()

        for (i in 0 until stops.size - 1) {
            val segment = bfsShortestPath(walkablePoints, stops[i], stops[i + 1])
            if (fullPath.isEmpty()) fullPath.addAll(segment)
            else fullPath.addAll(segment.drop(1))
        }

        return fullPath
    }



    fun bfsDistanceMap(
        walkablePoints: Set<Pair<Int, Int>>,
        start: Pair<Int, Int>
    ): Map<Pair<Int, Int>, Int> {

        val queue = ArrayDeque<Pair<Int, Int>>()
        val distances = mutableMapOf<Pair<Int, Int>, Int>()

        queue.add(start)
        distances[start] = 0

        val directions = listOf(
            1 to 0,
            -1 to 0,
            0 to 1,
            0 to -1
        )

        while (queue.isNotEmpty()) {

            val current = queue.removeFirst()
            val currentDistance = distances[current]!!

            for ((dx, dy) in directions) {

                val next =
                    (current.first + dx) to
                            (current.second + dy)

                if (
                    next in walkablePoints &&
                    next !in distances
                ) {

                    distances[next] = currentDistance + 1
                    queue.add(next)
                }
            }
        }

        return distances
    }
}