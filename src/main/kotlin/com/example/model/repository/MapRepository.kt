package com.example.model.repository

import com.example.model.entity.Map

interface MapRepository {
    suspend fun mapById(id:Int): Map?
    suspend fun addMap(map: Map): Map
    suspend fun removeMap(map: Map): Boolean
    suspend fun updateMap(map: Map): Map
    suspend fun mapsByStoreId(storeId: Int): List<Map>
}