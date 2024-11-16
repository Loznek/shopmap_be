package com.example.model.repository

import com.example.model.entity.Till


interface TillRepository {

    suspend fun tillsByMap(mapId:Int): List<Till>
    suspend fun addTill(wallBlock: Till)
    suspend fun removeTillById(id:Int): Boolean
    suspend fun updateTill(wallBlock: Till)

}