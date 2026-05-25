package com.example.stores

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.storeRoutes(controller: StoreController) {

    route("/store") {

        get("/{id}") {
            controller.get(call)
        }

        post {
            controller.create(call)
        }

        delete("/{id}") {
            controller.delete(call)
        }

        get("/{id}/place-details") {
            controller.fetchPlaceDetails(call)
        }

        get("/{id}/component-details") {
            controller.getDetails(call)
        }

        get(
            "/{storeId}/shopping-lists/{shoppingListId}/matches"
        ) {

            val storeId =
                call.parameters["storeId"]?.toIntOrNull()
                    ?: return@get call.respond(
                        HttpStatusCode.BadRequest
                    )

            val shoppingListId =
                call.parameters["shoppingListId"]?.toIntOrNull()
                    ?: return@get call.respond(
                        HttpStatusCode.BadRequest
                    )

            call.respond(
                controller.getMatches(
                    storeId,
                    shoppingListId
                )
            )
        }
    }
}