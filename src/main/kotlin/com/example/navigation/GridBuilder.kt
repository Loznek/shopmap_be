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

        for (x in 1 until (map.width + 1).toInt()) {
            for (y in 1 until (map.height + 1).toInt()) {

                val isInWall = wallBlocks
                    .filter { it.id != excludeWallId }
                    .any { wall ->
                        x in wall.startX.toInt() until (wall.startX + wall.width).toInt() &&
                                y in wall.startY.toInt() until (wall.startY + wall.height).toInt()
                    }

                val isInDepartment = departments
                    .filter { it.id != excludeDepartmentId }
                    .any { dep ->
                        x in dep.startX.toInt() until (dep.startX + dep.width).toInt() &&
                                y in dep.startY.toInt() until (dep.startY + dep.height).toInt()
                    }

                val isInNewObject = newRect?.let {
                    x in it.x.toInt() until (it.x + it.width).toInt() &&
                            y in it.y.toInt() until (it.y + it.height).toInt()
                } ?: false

                if (!isInWall && !isInDepartment && !isInNewObject) {
                    points.add(x to y)
                }
            }
        }

        // Ensure till is reachable
        points.add(tills[0].startX.toInt() to tills[0].startY.toInt())

        return points
    }
}