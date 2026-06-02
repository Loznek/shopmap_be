package com.example.stores

import com.example.exception.ValidationException
import com.example.stores.dto.CreateStoreRequest
import com.example.stores.dto.UpdateStoreRequest
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

    suspend fun getAll(call: ApplicationCall) {
        val stores = service.getAll()
        call.respond(stores.map { it.toResponse() })
    }

    suspend fun get(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: throw ValidationException(
                "Invalid store id"
            )
        val result = service.getById(id)
        call.respond(result.toResponse())
    }

    suspend fun create(call: ApplicationCall) {

            val request = call.receive<CreateStoreRequest>()
            val result = service.create(request.toEntity())

            call.respond(HttpStatusCode.Created, result.toResponse())
    }


    suspend fun update(call: ApplicationCall) {

            val request = call.receive<UpdateStoreRequest>()
            val result = service.update(request.toEntity())
            call.respond(HttpStatusCode.OK, result.toResponse())

    }

    suspend fun delete(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: throw ValidationException(
                "Invalid store id"
            )
        service.delete(id)
        call.respond(HttpStatusCode.NoContent)
    }

    suspend fun fetchPlaceDetails(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: throw ValidationException(
                "Invalid store id"
            )
        val details = storeGooglePlacesService.fetchAndStore(id)

        call.respond(HttpStatusCode.OK, details)
    }


    suspend fun getDetails(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: throw ValidationException(
                "Invalid store id"
            )
        val details = service.getStoreDetails(id)

        call.respond(HttpStatusCode.OK, details)
    }

    suspend fun getMatches(
        storeId: Int,
        shoppingListId: Int
    ) =
        service.getProductMatches(
            storeId,
            shoppingListId
        )

}