package com.example.model.repository

import com.example.model.entity.WallBlock

interface WallBlockRepository {
    suspend fun wallBlocksByMap(mapId:Int): List<WallBlock>
    suspend fun addWallBlock(wallBlock: WallBlock)
    suspend fun removeWallBlockById(id:Int): Boolean
    suspend fun updateWallBlock(wallBlock: WallBlock)
}