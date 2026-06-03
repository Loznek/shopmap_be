package com.example.maps.dto

import com.example.db.mapping.MapTable.exitX
import com.example.db.mapping.MapTable.exitY
import com.example.model.entity.Map
import com.example.navigation.roundToHalf

fun CreateMapRequest.toEntity() = com.example.model.entity.Map(
    id = null,
    width = width.roundToHalf(),
    height = height.roundToHalf(),
    entranceX = entranceX.roundToHalf(),
    entranceY = entranceY.roundToHalf(),
    exitX = exitX.roundToHalf(),
    exitY = exitY.roundToHalf(),
    storeId = storeId
)

fun UpdateMapRequest.toEntity() = com.example.model.entity.Map(
    id = id,
    width = width.roundToHalf(),
    height = height.roundToHalf(),
    entranceX = entranceX.roundToHalf(),
    entranceY = entranceY.roundToHalf(),
    exitX = exitX.roundToHalf(),
    exitY = exitY.roundToHalf(),
    storeId = storeId
)

fun Map.toResponse() = MapResponse(
    id = id,
    width = width,
    height = height,
    entranceX = entranceX,
    entranceY = entranceY,
    exitX = exitX,
    exitY = exitY,
    storeId = storeId
)