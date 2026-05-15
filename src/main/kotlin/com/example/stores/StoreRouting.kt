package com.example.stores

import io.ktor.server.application.*
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
    }
}