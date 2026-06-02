package com.example.recipes

import com.example.exception.ExternalServiceException
import com.example.exception.ValidationException
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import com.example.recipes.dto.IngredientResponse
import com.example.recipes.dto.OpenAiMessage
import com.example.recipes.dto.OpenAiRequest
import com.example.recipes.dto.OpenAiResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

class RecipeService(
    private val client: HttpClient,
    private val apiKey: String
) {



    suspend fun getIngredients(mealName: String): List<IngredientResponse> {

        if (mealName.isBlank()) {
            throw ValidationException(
                "Meal name cannot be empty"
            )
        }

        val prompt = """
            Give me the ingredients for $mealName.
            Respond ONLY in JSON format as:
            [
              {"name": "...", "amount": "..."}
            ]
            No explanation, no text, only JSON.
            Return ONLY valid JSON.
            Do not include markdown.
            Do not include explanation.
        """.trimIndent()

        val response: OpenAiResponse =

            try {

                client.post("https://api.openai.com/v1/chat/completions") {
                    header(HttpHeaders.Authorization, "Bearer $apiKey")
                    contentType(ContentType.Application.Json)

                    setBody(
                        OpenAiRequest(
                            model = "gpt-4o-mini",
                            messages = listOf(
                                OpenAiMessage(
                                    role = "user",
                                    content = prompt
                                )
                            ),
                            temperature = 0.2
                        )
                    )
                }.body()

            } catch (e: Exception) {
                throw ExternalServiceException(
                    "Failed to fetch ingredients from openAI: ${e.message} "
                )
            }


        val content =
            response.choices
                .firstOrNull()
                ?.message
                ?.content
                ?: throw ExternalServiceException(
                    "OpenAI returned empty response"
                )

        return try {
            parseIngredients(content)
        } catch (e: Exception) {
            throw ExternalServiceException(
                "OpenAI returned invalid ingredient list ${e.message}"
            )
        }
    }

    private fun parseIngredients(jsonText: String): List<IngredientResponse> {
        val json = Json { ignoreUnknownKeys = true }

        return json.decodeFromString(jsonText)
    }
}