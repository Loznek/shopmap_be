package com.example.wallblocks

import io.ktor.server.routing.*

fun Route.wallBlockRoutes(controller: WallBlockController) {

    route("/wall-blocks") {

        get("/item/{id}") {
            controller.get(call)
        }

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