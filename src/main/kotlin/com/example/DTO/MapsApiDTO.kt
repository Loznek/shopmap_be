package com.example.DTO
import kotlinx.serialization.Serializable


@Serializable
data class SearchTextRequest(
    val textQuery: String
)

@Serializable
data class SearchResponse(
    val places: List<PlaceSummary> = emptyList()
)

@Serializable
data class PlaceSummary(
    val id: String,
    val formattedAddress: String? = null,
    val displayName: DisplayName? = null
)

@Serializable
data class DisplayName(
    val text: String
)


@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double
)


@Serializable
data class PlaceDetailsResponse(
    val id: String,
    val internationalPhoneNumber: String? = null,
    val websiteUri: String? = null,
    val googleMapsUri: String? = null,
    val rating: Double? = null,
    val userRatingCount: Int? = null,
    val priceLevel: Int? = null,
    val photos: List<Photo>? = null,
    val regularOpeningHours: OpeningHours? = null,
    val parkingOptions: ParkingOptions? = null,
    val accessibilityOptions: AccessibilityOptions? = null
)

@Serializable
data class Photo(
    val name: String
)

@Serializable
data class ParkingOptions(
    val freeParkingLot: Boolean? = null,
    val freeStreetParking: Boolean? = null,
    val freeGarageParking: Boolean? = null
)

@Serializable
data class AccessibilityOptions(
    val wheelchairAccessibleEntrance: Boolean? = null
)

@Serializable
data class OpeningHours(
    val periods: List<Period>? = null
)

@Serializable
data class Period(
    val open: DayTime,
    val close: DayTime
)

@Serializable
data class DayTime(
    val day: Int,
    val hour: Int,
    val minute: Int
)