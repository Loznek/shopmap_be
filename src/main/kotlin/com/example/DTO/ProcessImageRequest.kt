package com.example.DTO

import kotlinx.serialization.Serializable

@Serializable
data class ProcessImageRequest(
    val imagePath: String,
    val mapWidth: Int,
    val mapHeight: Int,
    val mapId: Int
)