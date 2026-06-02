package com.example.tills

import com.example.departments.dto.toResponse
import com.example.exception.ValidationException
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

    suspend fun get(call: ApplicationCall) {

        val id = call.parameters["id"]?.toIntOrNull() ?: throw ValidationException("Invalid till id")
        val till = service.get(id)

        call.respond(till.toResponse())

    }

    suspend fun getByMap(call: ApplicationCall) {
        val mapId = call.parameters["mapId"]?.toIntOrNull()
            ?: throw ValidationException( "Invalid mapId")

        val result = service.getByMap(mapId)
        call.respond(result.map { it.toResponse() })
    }

    suspend fun create(call: ApplicationCall) {

            val request = call.receive<CreateTillRequest>()
            val result = service.create(request.toEntity())
            call.respond(HttpStatusCode.Created, result.toResponse())
    }

    suspend fun update(call: ApplicationCall) {

            val request = call.receive<UpdateTillRequest>()
            val result = service.update(request.toEntity())
            call.respond(HttpStatusCode.OK, result.toResponse())


    }

    suspend fun delete(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: throw ValidationException(
                "Invalid department id"
            )
        service.delete(id)
        call.respond(HttpStatusCode.NoContent)
    }
}