package com.example.model.entity

import kotlinx.serialization.Serializable


@Serializable
data class Map(
    var id: Int?,
    val storeId: Int,
    val width: Double,
    val height: Double,
    val entranceX: Double,
    val entranceY: Double,
    val exitX: Double,
    val exitY: Double
)