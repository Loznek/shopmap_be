package com.example.departments

import com.example.departments.dto.CreateDepartmentRequest
import com.example.departments.dto.UpdateDepartmentRequest
import com.example.departments.dto.toResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class DepartmentController(
    private val service: DepartmentService
) {

    suspend fun getByMap(call: ApplicationCall) {
        val mapId = call.parameters["mapId"]?.toIntOrNull()
            ?: return call.respond(HttpStatusCode.BadRequest, "Invalid mapId")

        val departments = service.getByMap(mapId)
        call.respond(departments.map { it.toResponse() })
    }

    suspend fun create(call: ApplicationCall) {
        val request = call.receive<CreateDepartmentRequest>()
        val result = service.create(request)
        call.respond(HttpStatusCode.Created, result.toResponse())
    }

    suspend fun update(call: ApplicationCall) {
        val request = call.receive<UpdateDepartmentRequest>()
        val result = service.update(request)
        call.respond(HttpStatusCode.OK, result.toResponse())
    }

    suspend fun delete(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(HttpStatusCode.BadRequest, "Invalid id")

        service.delete(id)
        call.respond(HttpStatusCode.NoContent)
    }
}