package com.example.model.repository

import com.example.db.mapping.*
import com.example.model.entity.OpeningHours

class PostgresOpeningHoursRepository : OpeningHoursRepository {

    override suspend fun getByStoreId(storeId: Int): List<OpeningHours> = suspendTransaction {
        OpeningHoursDAO.find { OpeningHoursTable.storeId eq storeId }
            .map(::openingHoursDaoToModel)
    }

    override suspend fun add(openingHours: OpeningHours): OpeningHours = suspendTransaction {
        val store = StoreDAO.findById(openingHours.storeId)
            ?: throw Exception("Store not found")

        val new = OpeningHoursDAO.new {
            this.store = store
            this.day = openingHours.day
            this.openTime = openingHours.openTime
            this.closeTime = openingHours.closeTime
        }

        openingHoursDaoToModel(new)
    }
}