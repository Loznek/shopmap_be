package com.example.stores

import com.example.stores.dto.CreateStoreRequest
import com.example.stores.dto.toEntity
import com.example.stores.dto.toResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class StoreController(
    private val service: StoreService,
    private val storeGooglePlacesService: StoreGooglePlacesService
) {

    suspend fun get(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(HttpStatusCode.BadRequest, "Invalid id")

        val result = service.getById(id)
        call.respond(result.toResponse())
    }

    suspend fun create(call: ApplicationCall) {
        try {
            val request = call.receive<CreateStoreRequest>()
            val result = service.create(request.toEntity())

            call.respond(HttpStatusCode.Created, result.toResponse())

        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid request")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid input data")
        }
    }

    suspend fun delete(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(HttpStatusCode.BadRequest, "Invalid id")

        service.delete(id)
        call.respond(HttpStatusCode.NoContent)
    }

    suspend fun fetchPlaceDetails(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(HttpStatusCode.BadRequest, "Invalid id")

        val details = storeGooglePlacesService.fetchAndStore(id)

        call.respond(HttpStatusCode.OK, details)
    }
}