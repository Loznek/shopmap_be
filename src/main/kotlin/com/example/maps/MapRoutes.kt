package com.example.maps

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.mapRoutes(controller: MapController) {

    route("/maps") {

        get("/{id}") {
            controller.get(call)
        }

        post {
            controller.create(call)
        }

        put {
            controller.update(call)
        }

        delete("/{id}") {
            controller.delete(call)
        }

        post("/import-by-image") {
            controller.processImage(call)
        }



    }
}