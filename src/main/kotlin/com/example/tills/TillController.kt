package com.example.tills

import com.example.tills.dto.CreateTillRequest
import com.example.tills.dto.UpdateTillRequest
import com.example.tills.dto.toEntity
import com.example.tills.dto.toResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class TillController(
    private val service: TillService
) {

    suspend fun getByMap(call: ApplicationCall) {
        val mapId = call.parameters["mapId"]?.toIntOrNull()
            ?: return call.respond(HttpStatusCode.BadRequest, "Invalid mapId")

        val result = service.getByMap(mapId)
        call.respond(result.map { it.toResponse() })
    }

    suspend fun create(call: ApplicationCall) {
        try {
            val request = call.receive<CreateTillRequest>()
            val result = service.create(request.toEntity())
            call.respond(HttpStatusCode.Created, result.toResponse())

        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid request")
        }
    }

    suspend fun update(call: ApplicationCall) {
        try {
            val request = call.receive<UpdateTillRequest>()
            val result = service.update(request.toEntity())
            call.respond(HttpStatusCode.OK, result.toResponse())

        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid request")
        }
    }

    suspend fun delete(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(HttpStatusCode.BadRequest, "Invalid id")

        service.delete(id)
        call.respond(HttpStatusCode.NoContent)
    }
}