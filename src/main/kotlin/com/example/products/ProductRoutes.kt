package com.example.products

import io.ktor.server.application.*
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.productRoutes(controller: ProductController) {
    route("/products") {

        post {
            controller.create(call)
        }

        get("/{articleNo}") {
            controller.getById(call)
        }

        get("/store/{storeId}") {
            controller.getByStore(call)
        }

        put("/{articleNo}") {
            controller.update(call)
        }

        delete("/{articleNo}") {
            controller.delete(call)
        }
    }
}