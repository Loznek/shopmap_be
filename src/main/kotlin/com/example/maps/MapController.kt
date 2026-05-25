package com.example.maps

import com.example.maps.dto.ProcessImageRequest
import com.example.maps.dto.CreateMapRequest
import com.example.maps.dto.UpdateMapRequest
import com.example.maps.dto.toEntity
import com.example.maps.dto.toResponse
import io.ktor.http.*
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class MapController(
    private val service: MapService
) {

    suspend fun get(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(HttpStatusCode.BadRequest, "Invalid id")

        val result = service.getById(id)
        call.respond(result.toResponse())
    }

    suspend fun create(call: ApplicationCall) {
        try {
            val request = call.receive<CreateMapRequest>()
            val result = service.create(request.toEntity())
            call.respond(HttpStatusCode.Created, result.toResponse())

        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid request")
        }
    }

    suspend fun update(call: ApplicationCall) {
        try {
            val request = call.receive<UpdateMapRequest>()
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


    suspend fun processImage(
        call: ApplicationCall
    ) {

        val multipart = call.receiveMultipart()

        var mapWidth: Int? = null
        var mapHeight: Int? = null
        var mapId: Int? = null

        var imageBytes: ByteArray? = null

        multipart.forEachPart { part ->

            when (part) {

                is PartData.FormItem -> {
                    when (part.name) {
                        "mapWidth" -> mapWidth = part.value.toInt()
                        "mapHeight" -> mapHeight = part.value.toInt()
                        "mapId" -> mapId = part.value.toInt()
                    }
                }

                is PartData.FileItem -> {
                    imageBytes =
                        part.streamProvider()
                            .readBytes()
                }

                else -> {}
            }

            part.dispose()
        }

        val request = ProcessImageRequest(
            mapWidth = mapWidth ?: error("Missing mapWidth"),
            mapHeight = mapHeight ?: error("Missing mapHeight"),
            mapId = mapId ?: error("Missing mapId")
        )

        val department = service.processImage(
            imageBytes ?: error("Missing image"),
            request
        )

        call.respond(
            HttpStatusCode.Created,
            department
        )
    }
}