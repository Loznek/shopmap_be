package com.example.DTO

import kotlinx.serialization.Serializable


@Serializable
class ShelfCreation (
        val departmentId: Int,
        val width: Double,
        val height: Double,
        val startX: Double,
        val startY:Double,
        val shelfType: OuterSide
)

@Serializable
enum class OuterSide {
    Up,
    Down,
    Left,
    Right,
}