package com.example.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class WallBlock(
    val id: Int?,
    val mapId: Int,
    val width: Double,
    val height: Double,
    val startX: Double,
    val startY: Double
)