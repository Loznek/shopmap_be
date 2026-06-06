package com.example.ocr

import com.example.exception.ValidationException
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray
import java.io.File

class OcrController(
    private val ocrService: OcrService
) {


    suspend fun extractShoppingList(call: ApplicationCall) {
        //val fileBytes = call.receive<ByteArray>()
        val multipart = call.receiveMultipart()

        var imageBytes: ByteArray? = null

        multipart.forEachPart { part ->

            when (part) {
                is PartData.FileItem -> {
                    imageBytes = part.provider()
                        .readRemaining()
                        .readByteArray()
                }

                else -> {}
            }

            part.dispose()
        }

        if (imageBytes == null) {
            throw ValidationException("No image provided")
        }

        val shoppingList = ocrService.extractShoppingListWithGoogle(imageBytes)

        call.respond(HttpStatusCode.OK, shoppingList)
    }
}