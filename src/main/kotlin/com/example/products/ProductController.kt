package com.example.products

import com.example.products.dto.CreateProductRequest
import com.example.products.dto.UpdateProductRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

class ProductController(
    private val productService: ProductService
) {

    suspend fun create(call: ApplicationCall) {
        val request = call.receive<CreateProductRequest>()
        val response = productService.create(request)
        call.respond(HttpStatusCode.Created, response)
    }

    suspend fun getById(call: ApplicationCall) {
        val articleNo = call.parameters["articleNo"]?.toIntOrNull()
            ?: return call.respond(HttpStatusCode.BadRequest, "Invalid articleNo")

        val response = productService.getById(articleNo)
        call.respond(HttpStatusCode.OK, response)
    }

    suspend fun getByStore(call: ApplicationCall) {
        val storeId = call.parameters["storeId"]?.toIntOrNull()
            ?: return call.respond(HttpStatusCode.BadRequest, "Invalid storeId")

        val response = productService.getByStore(storeId)
        call.respond(HttpStatusCode.OK, response)
    }

    suspend fun update(call: ApplicationCall) {
        val request = call.receive<UpdateProductRequest>()
        val response = productService.update(request)
        call.respond(HttpStatusCode.OK, response)
    }

    suspend fun delete(call: ApplicationCall) {
        val articleNo = call.parameters["articleNo"]?.toIntOrNull()
            ?: return call.respond(HttpStatusCode.BadRequest, "Invalid articleNo")

        productService.delete(articleNo)
        call.respond(HttpStatusCode.NoContent)
    }
}