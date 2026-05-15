package com.example.wallblocks.dto

data class CreateWallBlockRequest(
    val mapId: Int,
    val startX: Double,
    val startY: Double,
    val width: Double,
    val height: Double
)

data class UpdateWallBlockRequest(
    val id: Int,
    val mapId: Int,
    val startX: Double,
    val startY: Double,
    val width: Double,
    val height: Double
)