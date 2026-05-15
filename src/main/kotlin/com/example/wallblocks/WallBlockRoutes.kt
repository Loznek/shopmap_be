package com.example.wallblocks

import WallBlockController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.wallBlockRoutes(controller: WallBlockController) {

    route("/wall-blocks") {

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