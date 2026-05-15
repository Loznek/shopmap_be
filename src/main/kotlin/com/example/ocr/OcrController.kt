package com.example.ocr

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import java.io.File

class OcrController(
    private val ocrService: OcrService
) {

    suspend fun extractText(call: ApplicationCall) {
        val filePath = call.request.queryParameters["file"]
            ?: return call.respond(HttpStatusCode.BadRequest, "Missing file parameter")

        val file = File(filePath.replace("/", "\\"))

        val text = ocrService.extractTextWithTesseract(file)

        if (text.isBlank()) {
            call.respond(HttpStatusCode.BadRequest, "No text found in image")
        } else {
            call.respond(HttpStatusCode.OK, text)
        }
    }

    suspend fun extractShoppingList(call: ApplicationCall) {
        val fileBytes = call.receive<ByteArray>()
        val shoppingList = ocrService.extractShoppingListWithGoogle(fileBytes)

        call.respond(HttpStatusCode.OK, shoppingList)
    }
}