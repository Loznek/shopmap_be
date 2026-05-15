package com.example.tills.dto

import com.example.model.entity.Till

fun CreateTillRequest.toEntity() = Till(
    id = null,
    mapId = mapId,
    startX = startX,
    startY = startY,
    width = width,
    height = height
)

fun UpdateTillRequest.toEntity() = Till(
    id = id,
    mapId = mapId,
    startX = startX,
    startY = startY,
    width = width,
    height = height
)

fun Till.toResponse() = TillResponse(
    id = id,
    mapId = mapId,
    startX = startX,
    startY = startY,
    width = width,
    height = height
)