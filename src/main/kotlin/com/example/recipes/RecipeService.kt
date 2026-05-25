package com.example.recipes

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
    /*
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private val apiKey = ""
    */


    suspend fun getIngredients(mealName: String): List<IngredientResponse> {

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

        val response: OpenAiResponse = client.post("https://api.openai.com/v1/chat/completions") {
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

        val content = response.choices.first().message.content

        return parseIngredients(content)
    }

    private fun parseIngredients(jsonText: String): List<IngredientResponse> {
        val json = Json { ignoreUnknownKeys = true }

        return json.decodeFromString(jsonText)
    }
}