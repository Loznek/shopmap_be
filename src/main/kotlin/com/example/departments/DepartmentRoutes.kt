package com.example.departments

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.departmentRoutes(controller: DepartmentController) {

    route("/departments") {

        get("/{mapId}") {
            controller.getByMap(call)
        }

        post {
            controller.create(call)
        }

        put {
            controller.update(call)
        }

        delete("/{id}") {
            controller.delete(call)
        }
    }
}