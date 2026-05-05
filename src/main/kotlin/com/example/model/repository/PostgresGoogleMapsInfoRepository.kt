package com.example.model.repository

import com.example.db.mapping.*
import com.example.model.entity.GoogleMapsInfo

class PostgresGoogleMapsInfoRepository : GoogleMapsInfoRepository {

    override suspend fun getByStoreId(storeId: Int): GoogleMapsInfo? = suspendTransaction {
        GoogleMapsInfoDAO.find { GoogleMapsInfoTable.storeId eq storeId }
            .map(::googleMapsDaoToModel)
            .firstOrNull()
    }

    override suspend fun add(info: GoogleMapsInfo): GoogleMapsInfo = suspendTransaction {

        val store = StoreDAO.findById(info.storeId)
            ?: throw Exception("Store not found")

        val new = GoogleMapsInfoDAO.new {
            this.store = store
            this.placeId = info.placeId
            this.phoneNumber = info.phoneNumber
            this.websiteUri = info.websiteUri
            this.googleMapsUri = info.googleMapsUri
            this.rating = info.rating
            this.userRatingCount = info.userRatingCount
            this.hasParking = info.hasParking
            this.wheelchairAccessible = info.wheelchairAccessible
        }

        googleMapsDaoToModel(new)
    }
}