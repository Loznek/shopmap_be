package com.example.maps

import ProcessImageResponse
import PythonRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.*

class PythonMapProcessorClient(
    private val httpClient: HttpClient
) {

    suspend fun processImage(
        imagePath: String,
        mapWidth: Int,
        mapHeight: Int
    ): ProcessImageResponse {
        return httpClient.post("http://localhost:8001/process") {
            contentType(ContentType.Application.Json)
            setBody(
                PythonRequest(
                    image_path = imagePath,
                    map_width = mapWidth,
                    map_height = mapHeight
                )
            )
        }.body()
    }
}