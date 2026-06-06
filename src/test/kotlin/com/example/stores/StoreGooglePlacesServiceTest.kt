package com.example.stores

import com.example.exception.NotFoundException
import com.example.exception.ValidationException
import com.example.model.entity.GoogleMapsInfo
import com.example.model.entity.OpeningHours
import com.example.model.entity.Store
import com.example.model.entity.StorePicture
import com.example.model.repository.GoogleMapsInfoRepository
import com.example.model.repository.OpeningHoursRepository
import com.example.model.repository.StorePictureRepository
import com.example.model.repository.StoreRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class StoreGooglePlacesServiceTest {

    private val storeRepository =
        mockk<StoreRepository>()

    private val googleMapsInfoRepository =
        mockk<GoogleMapsInfoRepository>()

    private val openingHoursRepository =
        mockk<OpeningHoursRepository>()

    private val pictureRepository =
        mockk<StorePictureRepository>()

    private val googlePlacesClient =
        mockk<GooglePlacesClient>()

    private val photoDownloader =
        mockk<PhotoDownloader>()

    private val service =
        StoreGooglePlacesService(
            storeRepository,
            googleMapsInfoRepository,
            openingHoursRepository,
            pictureRepository,
            googlePlacesClient,
            photoDownloader
        )

    @Test
    fun `fetchAndStore throws NotFoundException when store not found`() = runTest {

        coEvery {
            storeRepository.storeById(1)
        } returns null

        assertFailsWith<NotFoundException> {
            service.fetchAndStore(1)
        }
    }

    @Test
    fun `fetchAndStore throws ValidationException when store has no location`() = runTest {

        coEvery {
            storeRepository.storeById(1)
        } returns Store(
            id = 1,
            name = "Aldi",
            location = null
        )

        assertFailsWith<ValidationException> {
            service.fetchAndStore(1)
        }
    }

    @Test
    fun `fetchAndStore returns cached data when already imported`() = runTest {

        coEvery {
            storeRepository.storeById(1)
        } returns Store(
            id = 1,
            name = "Aldi",
            location = "Budapest"
        )

        coEvery {
            googleMapsInfoRepository.getByStoreId(1)
        } returns GoogleMapsInfo(
            id = 1,
            storeId = 1,
            placeId = "place-id",
            phoneNumber = "+36123456789",
            websiteUri = "https://aldi.hu",
            googleMapsUri = "https://maps.google.com",
            rating = 4.5,
            userRatingCount = 100,
            hasParking = true,
            wheelchairAccessible = true
        )

        coEvery {
            openingHoursRepository.getByStoreId(1)
        } returns listOf(
            OpeningHours(
                id = 1,
                storeId = 1,
                day = 1,
                openTime = "08:00",
                closeTime = "20:00"
            )
        )

        coEvery {
            pictureRepository.getByStoreId(1)
        } returns listOf(
            StorePicture(
                id = 1,
                storeId = 1,
                path = "/images/store1.jpg"
            )
        )

        val result =
            service.fetchAndStore(1)

        assertEquals(
            "+36123456789",
            result.phoneNumber
        )

        assertEquals(
            1,
            result.openingHours.size
        )

        assertEquals(
            1,
            result.imagePaths.size
        )

        assertTrue(
            result.hasParking
        )

        assertTrue(
            result.wheelchairAccessible
        )
    }
}