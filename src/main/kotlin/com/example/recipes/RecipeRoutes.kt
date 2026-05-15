package com.example.recipes

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.recipeRoutes(controller: RecipeController) {

    route("/recipes") {

        post("/ingredients") {
            controller.getIngredients(call)
        }
    }
}