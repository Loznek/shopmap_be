package com.example.model.entity

data class Rectangle(
    val x: Double,
    val y: Double,
    val width: Double,
    val height: Double
) {
    fun intersects(other: Rectangle): Boolean {
        return x < other.x + other.width &&
                x + width > other.x &&
                y < other.y + other.height &&
                y + height > other.y
    }
}