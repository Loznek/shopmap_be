package com.example.sales

import com.example.exception.ValidationException
import io.ktor.client.HttpClient
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class FlyerScraperTest {

    private val scraper =
        FlyerScraper(
            mockk<HttpClient>()
        )

    @Test
    fun `getSales throws ValidationException for unknown store`() = runTest {

        assertFailsWith<ValidationException> {

            scraper.getSales(
                "UNKNOWN_STORE"
            )
        }
    }
}