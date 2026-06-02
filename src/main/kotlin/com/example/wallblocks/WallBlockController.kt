package com.example.wallblocks

import com.example.exception.ValidationException
import com.example.wallblocks.dto.CreateWallBlockRequest
import com.example.wallblocks.dto.UpdateWallBlockRequest
import com.example.wallblocks.dto.toEntity
import com.example.wallblocks.dto.toResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class WallBlockController(
    private val service: WallBlockService
) {

    suspend fun get(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: throw ValidationException("Invalid wallblock id")
        val wallBlock = service.get(id)

        call.respond(wallBlock.toResponse())
    }

    suspend fun getByMap(call: ApplicationCall) {
        val mapId = call.parameters["mapId"]?.toIntOrNull()
            ?: throw ValidationException( "Invalid mapId")

        val result = service.getByMap(mapId)
        call.respond(result.map { it.toResponse() })
    }

    suspend fun create(call: ApplicationCall) {
            val request = call.receive<CreateWallBlockRequest>()
            val result = service.create(request.toEntity())
            call.respond(HttpStatusCode.Created, result.toResponse())

    }

    suspend fun update(call: ApplicationCall) {
            val request = call.receive<UpdateWallBlockRequest>()
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