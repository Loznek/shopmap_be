package com.example.navigation



import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.navigationRoutes(
    controller: NavigationController
) {
    route("/calculate-route") {
        post {
            controller.calculateRoute(call)
        }
    }
}