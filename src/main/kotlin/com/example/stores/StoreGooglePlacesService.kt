package com.example.stores


import com.example.model.entity.GoogleMapsInfo
import com.example.model.entity.OpeningHours
import com.example.model.entity.StorePicture
import com.example.model.repository.GoogleMapsInfoRepository
import com.example.model.repository.OpeningHoursRepository
import com.example.model.repository.StorePictureRepository
import com.example.model.repository.StoreRepository
import com.example.stores.google.PlaceDetailsResponse

class StoreGooglePlacesService(
    private val storeRepository: StoreRepository,
    private val googleMapsInfoRepository: GoogleMapsInfoRepository,
    private val openingHoursRepository: OpeningHoursRepository,
    private val pictureRepository: StorePictureRepository,
    private val googlePlacesClient: GooglePlacesClient,
    private val photoDownloader: PhotoDownloader
) {

    suspend fun fetchAndStore(storeId: Int): PlaceDetailsResponse {

        // 1. Load store
        val store = storeRepository.storeById(storeId)
            ?: throw IllegalArgumentException("Store not found")

        val location = store.location
            ?: throw IllegalArgumentException("Store has no location")

        // 2. Check if already imported
        val existing = googleMapsInfoRepository.getByStoreId(store.id!!)
        if (existing != null) {
            return googlePlacesClient.getPlaceDetails(existing.placeId)
        }

        // 3. Search Google Places
        val searchResponse = googlePlacesClient.searchPlace(
            storeName = store.name,
            location = location
        )

        val placeId = searchResponse.places.firstOrNull()?.id
            ?: throw IllegalArgumentException("No place found")

        // 4. Fetch detailed information
        val detailsResponse = googlePlacesClient.getPlaceDetails(placeId)

        // 5. Derived properties
        val hasParking = detailsResponse.parkingOptions?.let {
            (it.freeParkingLot == true) ||
                    (it.freeStreetParking == true) ||
                    (it.freeGarageParking == true)
        } ?: false

        val wheelchairAccessible =
            detailsResponse.accessibilityOptions?.wheelchairAccessibleEntrance == true

        // 6. Download photos
        val imagePaths = detailsResponse.photos
            ?.take(3)
            ?.mapIndexed { index, photo ->
                photoDownloader.download(
                    photoName = photo.name,
                    storeId = store.id!!,
                    index = index
                )
            }
            ?: emptyList()

        // 7. Save Google Maps info
        googleMapsInfoRepository.add(
            GoogleMapsInfo(
                id = null,
                storeId = store.id!!,
                placeId = detailsResponse.id,
                phoneNumber = detailsResponse.internationalPhoneNumber,
                websiteUri = detailsResponse.websiteUri,
                googleMapsUri = detailsResponse.googleMapsUri,
                rating = detailsResponse.rating,
                userRatingCount = detailsResponse.userRatingCount,
                hasParking = hasParking,
                wheelchairAccessible = wheelchairAccessible
            )
        )

        // 8. Save opening hours
        detailsResponse.regularOpeningHours?.periods?.forEach { period ->
            openingHoursRepository.add(
                OpeningHours(
                    id = null,
                    storeId = store.id,
                    day = period.open.day,
                    openTime = formatTime(period.open.hour, period.open.minute),
                    closeTime = formatTime(period.close.hour, period.close.minute)
                )
            )
        }

        // 9. Save pictures
        imagePaths.forEach { path ->
            pictureRepository.add(
                StorePicture(
                    id = null,
                    storeId = store.id,
                    path = path
                )
            )
        }

        // 10. Return Google response
        return detailsResponse
    }

    private fun formatTime(hour: Int, minute: Int): String {
        return "$hour:${minute.toString().padStart(2, '0')}"
    }
}