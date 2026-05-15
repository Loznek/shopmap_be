package com.example.stores


import com.example.stores.google.PlaceDetailsResponse
import com.example.stores.google.SearchResponse
import com.example.stores.google.SearchTextRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class GooglePlacesClient(
    private val client: HttpClient,
    private val apiKey: String
) {

    suspend fun searchPlace(
        storeName: String,
        location: String
    ): SearchResponse {
        return client.post("https://places.googleapis.com/v1/places:searchText") {
            header("X-Goog-Api-Key", apiKey)
            header(
                "X-Goog-FieldMask",
                "places.id,places.displayName,places.formattedAddress"
            )
            contentType(ContentType.Application.Json)
            setBody(
                SearchTextRequest(
                    textQuery = "$storeName $location"
                )
            )
        }.body()
    }

    suspend fun getPlaceDetails(
        placeId: String
    ): PlaceDetailsResponse {
        return client.get("https://places.googleapis.com/v1/places/$placeId") {
            header("X-Goog-Api-Key", apiKey)
            header(
                "X-Goog-FieldMask",
                "id,internationalPhoneNumber,websiteUri,googleMapsUri," +
                        "regularOpeningHours,rating,userRatingCount,priceLevel," +
                        "photos,accessibilityOptions,parkingOptions"
            )
        }.body()
    }
}