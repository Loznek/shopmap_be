package com.example.sales

import com.example.sales.dto.SalesResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

class SalesController(
    private val salesService: SalesService
) {

    suspend fun getSales(call: ApplicationCall) {
        val url =
            call.request.queryParameters["url"]
                ?: "https://akcios-ujsag.hu/akcios-ujsagok/aldi-akcios-ujsag-2026-03-12-03-18/"

        val items = salesService.parseSales(url)

        call.respond(
            HttpStatusCode.OK,
            SalesResponse(items)
        )
    }
}