package com.example.model.repository

import com.example.model.entity.GoogleMapsInfo

interface GoogleMapsInfoRepository {
    suspend fun getByStoreId(storeId: Int): GoogleMapsInfo?
    suspend fun add(info: GoogleMapsInfo): GoogleMapsInfo
}