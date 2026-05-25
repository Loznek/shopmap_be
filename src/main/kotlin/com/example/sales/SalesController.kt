package com.example.sales

import com.example.sales.dto.SalesResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

class SalesController(
    private val salesService: SalesService
) {

    suspend fun getSales(call: ApplicationCall) {

        val store =
            call.parameters["store"]
                ?: return call.respond(
                    HttpStatusCode.BadRequest,
                    "Store is required"
                )

        try {
            val response = salesService.getSales(store)

            call.respond(
                HttpStatusCode.OK,
                response
            )

        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                e.message ?: "Invalid store"
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                e.message ?: "Failed to fetch sales"
            )
        }
    }
}