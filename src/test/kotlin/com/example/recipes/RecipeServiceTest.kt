package com.example.recipes

import com.example.exception.ValidationException
import io.ktor.client.HttpClient
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class RecipeServiceTest {

    private val client = mockk<HttpClient>()

    private val service =
        RecipeService(
            client = client,
            apiKey = "test-key"
        )

    @Test
    fun `getIngredients throws ValidationException when meal name is blank`() = runTest {

        assertFailsWith<ValidationException> {
            service.getIngredients("")
        }
    }
}