package com.example.model.repository

import com.example.model.entity.OpeningHours

interface OpeningHoursRepository {
    suspend fun getByStoreId(storeId: Int): List<OpeningHours>
    suspend fun add(openingHours: OpeningHours): OpeningHours
}