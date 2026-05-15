package com.example.navigation

import com.example.model.entity.Department
import com.example.model.entity.Map
import com.example.model.entity.Till
import com.example.model.entity.WallBlock

class PathValidator(
    private val pathFinder: PathFinder
) {

    fun pathExists(
        walkablePoints: Set<Pair<Int, Int>>,
        start: Pair<Int, Int>,
        end: Pair<Int, Int>
    ): Boolean {
        return pathFinder.bfsShortestPath(walkablePoints, start, end).isNotEmpty()
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
        return pathFinder.bfsShortestPath(
            points.toSet(),
            Pair(map.entranceX.toInt(), map.entranceY.toInt()),
            Pair(tills[0].startX.toInt(), tills[0].startY.toInt())
        ).isNotEmpty()

    }


}