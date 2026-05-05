package com.example.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class GoogleMapsInfo(
    val id: Int?,
    val storeId: Int,
    val placeId: String,
    val phoneNumber: String?,
    val websiteUri: String?,
    val googleMapsUri: String?,
    val rating: Double?,
    val userRatingCount: Int?,
    val hasParking: Boolean,
    val wheelchairAccessible: Boolean
)