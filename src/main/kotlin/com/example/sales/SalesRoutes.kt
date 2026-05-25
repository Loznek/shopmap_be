package com.example.sales

import io.ktor.server.application.*
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.salesRoutes(controller: SalesController) {
    route("/sales/{store}") {
        get {
            controller.getSales(call)
        }
    }
}