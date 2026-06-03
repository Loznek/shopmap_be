package com.example.wallblocks.dto

import com.example.model.entity.WallBlock
import com.example.navigation.roundToHalf

fun CreateWallBlockRequest.toEntity() = WallBlock(
    id = null,
    mapId = mapId,
    startX = startX.roundToHalf(),
    startY = startY.roundToHalf(),
    width = width.roundToHalf(),
    height = height.roundToHalf()
)

fun UpdateWallBlockRequest.toEntity() = WallBlock(
    id = id,
    mapId = mapId,
    startX = startX.roundToHalf(),
    startY = startY.roundToHalf(),
    width = width.roundToHalf(),
    height = height.roundToHalf()
)

fun WallBlock.toResponse() = WallBlockResponse(
    id = id,
    mapId = mapId,
    startX = startX,
    startY = startY,
    width = width,
    height = height
)