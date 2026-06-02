package com.example.maps

import com.example.exception.ValidationException
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
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray

class MapController(
    private val service: MapService
) {

    suspend fun get(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: throw ValidationException("Invalid map id")

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
            ?: throw ValidationException("Invalid map id")

        service.delete(id)
        call.respond(HttpStatusCode.NoContent)
    }


    suspend fun processImage(
        call: ApplicationCall
    ) {

        val multipart = call.receiveMultipart()

        var mapWidth: Double? = null
        var mapHeight: Double? = null
        var mapId: Int? = null

        var imageBytes: ByteArray? = null
        multipart.forEachPart { part ->

            when (part) {

                is PartData.FormItem -> {
                    when (part.name) {
                        "mapWidth" -> mapWidth = part.value.toDoubleOrNull()
                            ?: throw ValidationException(
                                "Invalid mapWidth"
                            )
                        "mapHeight" -> mapHeight = part.value.toDoubleOrNull()
                            ?: throw ValidationException(
                                "Invalid mapHeight"
                            )
                        "mapId" -> mapId = part.value.toInt()
                    }
                }

                is PartData.FileItem -> {
                    imageBytes =
                        part.provider()
                            .readRemaining()
                            .readByteArray()
                }

                else -> {}
            }

            part.dispose()
        }

        val request = ProcessImageRequest(
            mapWidth = mapWidth ?: throw ValidationException(
                "Missing mapWidth"
            ),
            mapHeight = mapHeight ?: throw ValidationException(
                "Missing mapHeight"
            ),
            mapId = mapId ?: throw ValidationException(
                "Missing mapId"
            )
        )


        val department = service.processImage(
            imageBytes ?: throw ValidationException(
                "Missing image"
            ),
            request
        )

        call.respond(
            HttpStatusCode.Created,
            department
        )
    }
}