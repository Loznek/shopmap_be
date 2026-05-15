package com.example.tills

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.tillRoutes(controller: TillController) {

    route("/tills") {

        get("/{mapId}") {
            controller.getByMap(call)
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
    }
}