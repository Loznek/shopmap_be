package com.example.geometry

import com.example.model.entity.Map
import com.example.model.entity.Rectangle

object SpatialValidator {

    fun isValidPosition(
        rect: Rectangle,
        map: Map,
        obstacles: List<Rectangle>
    ): Boolean {

        if (rect.x <= 0 || rect.y <= 0 || rect.width <= 0 || rect.height <= 0) {
            return false
        }

        if (rect.x + rect.width > map.width + 1 ||
            rect.y + rect.height > map.height + 1) {
            return false
        }

        return obstacles.none { it.intersects(rect) }
    }


    fun canResizeMap(
        newWidth: Double,
        newHeight: Double,
        objects: List<Rectangle>
    ): Boolean {

        if (newWidth < 0 || newHeight < 0) return false

        return objects.all {
            it.x + it.width <= newWidth &&
                    it.y + it.height <= newHeight
        }
    }
}