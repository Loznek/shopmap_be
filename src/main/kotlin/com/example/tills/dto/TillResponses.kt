package com.example.tills.dto

data class TillResponse(
    val id: Int?,
    val mapId: Int,
    val startX: Double,
    val startY: Double,
    val width: Double,
    val height: Double
)
