package com.example.ocr

import io.ktor.server.application.*
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.ocrRoutes(controller: OcrController) {

    route("/ocr") {
        get {
            controller.extractText(call)
        }

        post("/google") {
            controller.extractShoppingList(call)
        }
    }
}