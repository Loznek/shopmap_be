package com.example.maps.dto

import com.example.db.mapping.MapTable.exitX
import com.example.db.mapping.MapTable.exitY
import com.example.model.entity.Map

fun CreateMapRequest.toEntity() = com.example.model.entity.Map(
    id = null,
    width = width,
    height = height,
    entranceX = entranceX,
    entranceY = entranceY,
    exitX = exitX,
    exitY = exitY,
    storeId = storeId
)

fun UpdateMapRequest.toEntity() = com.example.model.entity.Map(
    id = id,
    width = width,
    height = height,
    entranceX = entranceX,
    entranceY = entranceY,
    exitX = exitX,
    exitY = exitY,
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