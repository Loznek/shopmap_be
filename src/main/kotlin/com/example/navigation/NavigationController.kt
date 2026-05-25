package com.example.navigation

import com.example.navigation.dto.RoutePlanResponse
import com.example.navigation.dto.RoutePlanningRequest
import com.example.navigation.dto.toModel
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

class NavigationController(
    private val navigationService: NavigationService
) {

    suspend fun calculateRoute(call: ApplicationCall) {
        val request = call.receive<RoutePlanningRequest>()

        val route = navigationService.calculateRoute(
            mapId = request.mapId,
            products = request.products.map { it.toModel() }
        )

        call.respond(
            HttpStatusCode.OK,
            RoutePlanResponse(route)
        )
    }
}