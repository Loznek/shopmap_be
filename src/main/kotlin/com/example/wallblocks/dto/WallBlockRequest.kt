package com.example.wallblocks.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateWallBlockRequest(
    val mapId: Int,
    val startX: Double,
    val startY: Double,
    val width: Double,
    val height: Double
)
@Serializable
data class UpdateWallBlockRequest(
    val id: Int,
    val mapId: Int,
    val startX: Double,
    val startY: Double,
    val width: Double,
    val height: Double
)