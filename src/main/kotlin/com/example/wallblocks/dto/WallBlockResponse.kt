package com.example.wallblocks.dto

data class WallBlockResponse(
    val id: Int?,
    val mapId: Int,
    val startX: Double,
    val startY: Double,
    val width: Double,
    val height: Double
)