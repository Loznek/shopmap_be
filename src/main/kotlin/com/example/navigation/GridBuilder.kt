package com.example.navigation

import com.example.model.entity.Department
import com.example.model.entity.Map
import com.example.model.entity.Rectangle
import com.example.model.entity.Till
import com.example.model.entity.WallBlock

class GridBuilder {

    fun buildWalkablePoints(
        map: Map,
        wallBlocks: List<WallBlock>,
        departments: List<Department>,
        tills: List<Till>,
        newRect: Rectangle?,
        excludeWallId: Int,
        excludeDepartmentId: Int
    ): Set<Pair<Int, Int>> {

        val points = mutableSetOf<Pair<Int, Int>>()

        for (x in 0 until map.width.toGrid()) {
            for (y in 0 until map.height.toGrid()) {

                val isInWall = wallBlocks
                    .filter { it.id != excludeWallId }
                    .any { wall ->
                        x in wall.startX.toGrid() until (wall.startX + wall.width).toGrid() &&
                                y in wall.startY.toGrid() until (wall.startY + wall.height).toGrid()
                    }

                val isInDepartment = departments
                    .filter { it.id != excludeDepartmentId }
                    .any { dep ->
                        x in dep.startX.toGrid() until (dep.startX + dep.width).toGrid() &&
                                y in dep.startY.toGrid() until (dep.startY + dep.height).toGrid()
                    }

                val isInNewObject = newRect?.let {
                    x in it.x.toGrid() until (it.x + it.width).toGrid() &&
                            y in it.y.toGrid() until (it.y + it.height).toGrid()
                } ?: false

                if (!isInWall && !isInDepartment && !isInNewObject) {
                    points.add(x to y)
                }
            }
        }

        val startPoint = map.entranceX.toGrid() to map.entranceY.toGrid()

        val till = tills[0]
        // Ensure till is reachable
        val endPoint= (till.startX + till.width / 2).toGrid() to (till.startY + till.height / 2).toGrid()
        if (startPoint !in points) {
            throw IllegalStateException("Entrance is blocked")
        }

        if (endPoint !in points) {
            throw IllegalStateException("Till area is blocked")
        }
        //points.add(startPoint)
        //points.add(endPoint)
        return points
    }
}