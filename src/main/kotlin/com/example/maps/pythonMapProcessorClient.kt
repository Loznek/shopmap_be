package com.example.maps

import com.example.exception.ExternalServiceException
import com.example.maps.dto.ProcessImageResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.*

class PythonMapProcessorClient(
    private val httpClient: HttpClient,
    private val pythonEndpoint: String
) {


        suspend fun processImage(
            imageBytes: ByteArray,
            mapWidth: Double,
            mapHeight: Double
        ): ProcessImageResponse {
            try {
                return httpClient.post(
                    "${pythonEndpoint}/process"
                ) {

                    setBody(
                        MultiPartFormDataContent(
                            formData {

                                append(
                                    "map_width",
                                    mapWidth.toString()
                                )

                                append(
                                    "map_height",
                                    mapHeight.toString()
                                )

                                append(
                                    "file",
                                    imageBytes,
                                    Headers.build {
                                        append(
                                            HttpHeaders.ContentDisposition,
                                            "filename=image.png"
                                        )
                                        append(
                                            HttpHeaders.ContentType,
                                            "image/png"
                                        )
                                    }
                                )
                            }
                        )
                    )
                }.body()

            } catch (e: Exception) {
                throw ExternalServiceException(
                    "Python image processor unavailable: ${e.message}"
                )
            }

        }
    }
