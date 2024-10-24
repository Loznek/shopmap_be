package com.example.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class Shelf(
    val id: Int,
    val departmentId: Int,
    val width: Double,
    val height: Double,
    val startX: Double,
    val startY:Double
)