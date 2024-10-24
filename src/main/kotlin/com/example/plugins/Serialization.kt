package com.example.plugins


import com.example.model.entity.*
import com.example.model.entity.Map
import com.example.model.repository.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException

fun Application.configureSerialization(departmentRepository: PostgresDepartmentRepository, mapRepository: PostgresMapRepository, storeRepository: PostgresStoreRepository, wallBlockRepository: PostgresWallBlockRepository, tillRepository:PostgresTillRepository) {

    routing {
        route("/departments") {
            get ("/{mapId}"){
                val departments = departmentRepository.departmentsByMap(call.parameters["mapId"]?.toInt() ?: 0)
                call.respond(departments)
            }

            delete("/{departmentId}") {
                departmentRepository.removeDepartmentById(call.parameters["departmentId"]?.toInt() ?: 0)
                call.respond(HttpStatusCode.NoContent)
            }

            post {
                try {
                    val department = call.receive<Department>()
                   departmentRepository.addDepartment(department)
                    call.respond(HttpStatusCode.NoContent)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }

        route("/maps") {
            get("/{id}") {
                val map = mapRepository.mapById(call.parameters["id"]?.toInt() ?: 0)
                call.respond(map!!)
            }

            delete("/{id}") {
                val map = mapRepository.mapById(call.parameters["id"]?.toInt() ?: 0)
                if (map != null) {
                    mapRepository.removeMap(map)
                }
                call.respond(HttpStatusCode.NoContent)
            }

            post {
                try {
                    val map = call.receive<Map>()
                    mapRepository.addMap(map)
                    call.respond(HttpStatusCode.NoContent)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
            route("/wall-blocks") {
                get ("/{mapId}"){
                    val wallBlocks = wallBlockRepository.wallBlocksByMap(call.parameters["mapId"]?.toInt() ?: 0)
                    call.respond(wallBlocks)
                }

                delete("/{wallBlockId}") {
                    wallBlockRepository.removeWallBlockById(call.parameters["wallBlockId"]?.toInt() ?: 0)
                    call.respond(HttpStatusCode.NoContent)
                }

                post {
                    try {
                        val wallBlock = call.receive<WallBlock>()
                        wallBlockRepository.addWallBlock(wallBlock)
                        call.respond(HttpStatusCode.NoContent)
                    } catch (ex: IllegalStateException) {
                        call.respond(HttpStatusCode.BadRequest)
                    } catch (ex: JsonConvertException) {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
            }
        route("/tills") {
            get ("/{tillId}"){
                val tills = tillRepository.tillsByMap(call.parameters["tillId"]?.toInt() ?: 0)
                call.respond(tills)
            }

            delete("/{tillId}") {
                tillRepository.removeTillById(call.parameters["tillId"]?.toInt() ?: 0)
                call.respond(HttpStatusCode.NoContent)
            }

            post {
                try {
                    val till = call.receive<Till>()
                    tillRepository.addTill(till)
                    call.respond(HttpStatusCode.NoContent)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
        route("/store") {
            get("/{id}") {
                val store = storeRepository.storeById(call.parameters["id"]?.toInt() ?: 0)
                call.respond(store!!)
            }

            delete("/{id}") {
               val store = storeRepository.storeById(call.parameters["id"]?.toInt() ?: 0)
                if (store != null) {
                    storeRepository.removeStore(store)
                }
                call.respond(HttpStatusCode.NoContent)
            }

            post {
                try {
                    val map = call.receive<Store>()
                    storeRepository.addStore(map)
                    call.respond(HttpStatusCode.NoContent)

                }
                catch (e: Exception) {
                    println("Serialization error: ${e.message}") // Log the serialization error
                    call.respond(HttpStatusCode.BadRequest, "Invalid input data: ${e.message}")
                }
                catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }

    }
}