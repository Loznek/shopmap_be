package com.example.tills.dto

import com.example.model.entity.Till
import com.example.navigation.roundToHalf

fun CreateTillRequest.toEntity() = Till(
    id = null,
    mapId = mapId,
    startX = startX.roundToHalf(),
    startY = startY.roundToHalf(),
    width = width.roundToHalf(),
    height = height.roundToHalf()
)

fun UpdateTillRequest.toEntity() = Till(
    id = id,
    mapId = mapId,
    startX = startX.roundToHalf(),
    startY = startY.roundToHalf(),
    width = width.roundToHalf(),
    height = height.roundToHalf()
)

fun Till.toResponse() = TillResponse(
    id = id,
    mapId = mapId,
    startX = startX,
    startY = startY,
    width = width,
    height = height
)