package com.example.wallblocks.dto

import com.example.model.entity.WallBlock

fun CreateWallBlockRequest.toEntity() = WallBlock(
    id = null,
    mapId = mapId,
    startX = startX,
    startY = startY,
    width = width,
    height = height
)

fun UpdateWallBlockRequest.toEntity() = WallBlock(
    id = id,
    mapId = mapId,
    startX = startX,
    startY = startY,
    width = width,
    height = height
)

fun WallBlock.toResponse() = WallBlockResponse(
    id = id,
    mapId = mapId,
    startX = startX,
    startY = startY,
    width = width,
    height = height
)