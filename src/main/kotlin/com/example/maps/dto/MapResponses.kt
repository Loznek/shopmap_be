package com.example.maps.dto

import kotlinx.serialization.Serializable


@Serializable
data class MapResponse(
    val id: Int?,
    val width: Double,
    val height: Double,
    val entranceX: Double,
    val entranceY: Double,
    val exitX: Double,
    val exitY: Double,
    val storeId: Int
)