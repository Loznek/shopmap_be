package com.example.recipes

import com.example.recipes.dto.RecipeRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class RecipeController(
    private val service: RecipeService
) {

    suspend fun getIngredients(call: ApplicationCall) {
        try {
            val request = call.receive<RecipeRequest>()

            val result = service.getIngredients(request.mealName)

            call.respond(result)

        } catch (e: Exception) {
            e.printStackTrace() // 👈 important
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Unknown error")
        }
    }
}