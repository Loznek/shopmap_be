package com.example.tills.dto

data class CreateTillRequest(
    val mapId: Int,
    val startX: Double,
    val startY: Double,
    val width: Double,
    val height: Double
)

data class UpdateTillRequest(
    val id: Int,
    val mapId: Int,
    val startX: Double,
    val startY: Double,
    val width: Double,
    val height: Double
)