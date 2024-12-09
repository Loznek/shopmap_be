package com.example.plugins


import com.example.DTO.RoutePlan
import com.example.DTO.RoutePlanning
import com.example.algorithms.PositionChecker
import com.example.algorithms.RouteCalculation
import com.example.model.entity.*
import com.example.model.entity.Map
import com.example.model.repository.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(departmentRepository: PostgresDepartmentRepository, mapRepository: PostgresMapRepository, storeRepository: PostgresStoreRepository, wallBlockRepository: PostgresWallBlockRepository, tillRepository: PostgresTillRepository) {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        route("/calculate-route") {
            post {
                val route = call.receive<RoutePlanning>()
                val map = mapRepository.mapById(route.mapId)
                val departments = departmentRepository.departmentsByMap(route.mapId)

                val tills = tillRepository.tillsByMap(route.mapId)
                val wallBlocks = wallBlockRepository.wallBlocksByMap(route.mapId)
                val path = RouteCalculation().calculateShortestRoutes(
                    map!!,
                    tills,
                    wallBlocks,
                    departments,
                    route.departmentIds
                )
                val planning: RoutePlan = RoutePlan(path)
                call.respond(HttpStatusCode.OK, planning)

            }
        }
        route("/departments") {
            get("/{mapId}") {
                val departments = departmentRepository.departmentsByMap(
                    call.parameters["mapId"]?.toInt() ?: 0
                )
                call.respond(departments)
            }

            delete("/{departmentId}") {
                departmentRepository.removeDepartmentById(
                    call.parameters["departmentId"]?.toInt() ?: 0
                )
                call.respond(HttpStatusCode.NoContent)
            }

            put("/{id}") {
                val department = call.receive<Department>()

                val checked = PositionChecker.checkNewBlockposition(
                    department.startX,
                    department.startY,
                    department.width,
                    department.height,
                    wallBlockRepository.wallBlocksByMap(department.mapId),
                    departmentRepository.departmentsByMap(department.mapId),
                    tillRepository.tillsByMap(department.mapId),
                    mapRepository.mapById(department.mapId)!!
                )
                if (!checked){
                    call.respond(HttpStatusCode.BadRequest, "Invalid position")
                }
                else{
                    val map = mapRepository.mapById(department.mapId)
                    val departments = departmentRepository.departmentsByMap(department.mapId)
                    val tills = tillRepository.tillsByMap(department.mapId)
                    val wallBlocks = wallBlockRepository.wallBlocksByMap(department.mapId)

                    if(!RouteCalculation().checkPathExistence( department.startX,
                            department.startY,
                            department.width,
                            department.height, map!!,
                            tills,
                            wallBlocks,
                            departments, department.id ?: -1, -1)) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid position, there is no path to the tills")
                    }

                    else {
                        val newDepartment = departmentRepository.updateDepartment(department)
                        call.respond(HttpStatusCode.Created, newDepartment)
                    }
                }

            }
            post {
                try {
                    val department = call.receive<Department>()
                    val checked = PositionChecker.checkNewBlockposition(
                        department.startX,
                        department.startY,
                        department.width,
                        department.height,
                        wallBlockRepository.wallBlocksByMap(department.mapId),
                        departmentRepository.departmentsByMap(department.mapId),
                        tillRepository.tillsByMap(department.mapId),
                        mapRepository.mapById(department.mapId)!!
                    )
                    if (!checked) call.respond(HttpStatusCode.BadRequest, "Invalid position")

                    else if(!RouteCalculation().checkPathExistence( department.startX,
                            department.startY,
                            department.width,
                            department.height, mapRepository.mapById(department.mapId)!!,
                            tillRepository.tillsByMap(department.mapId),
                            wallBlockRepository.wallBlocksByMap(department.mapId),
                            departmentRepository.departmentsByMap(department.mapId), department.id ?: -1, -1)) {
                       call.respond(HttpStatusCode.BadRequest, "Invalid position, there is no path to the tills")
                    }

                    else {
                        val newDepartment = departmentRepository.addDepartment(department)
                        call.respond(HttpStatusCode.Created, newDepartment)
                    }
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
        /*
Blokk mozgatása/létrehozása: belső alg, ami csekoolja, hogy mehet-e oda
Amikor letrejon egy Bolt, annak kell legyen egy bejarata, egy kijarata es egy kasszaja
Az utolsot ezekbol sose lehet torolni
Ezutan utkereso algoritmust kitalalni: melyik lesz ra a jo? A-bol B-be kell menni, es kozben pedig pontokat kell erinteni
*/
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



            put("/{id}") {
                val map = call.receive<Map>()
                val checked = PositionChecker.checkNewMapSizes(
                    map.width,
                    map.height,
                    wallBlockRepository.wallBlocksByMap(map.id!!),
                    departmentRepository.departmentsByMap(map.id!!),
                    tillRepository.tillsByMap(map.id!!),
                    mapRepository.mapById(map.id!!)!!
                )
                if (checked) {
                    val newMap= mapRepository.updateMap(map)
                    call.respond(HttpStatusCode.Created, newMap)
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "Map resizing would cause collision"
                    )
                }
                //PositionChecker.checkNewBlockposition(map, wallBlockRepository.wallBlocksByMap(map.id), departmentRepository.departmentsByMap(map.id), tillRepository.tillsByMap(map.id), map)
                //mapRepository.updateMap(map)

            }

            post {
                try {
                    val map = call.receive<Map>()
                    val newMap = mapRepository.addMap(map)
                    call.respond(HttpStatusCode.Created, newMap)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
        route("/wall-blocks") {
            get("/{mapId}") {
                val wallBlocks =
                    wallBlockRepository.wallBlocksByMap(call.parameters["mapId"]?.toInt() ?: 0)
                call.respond(wallBlocks)
            }
            put("/{id}") {
                val wallBlock = call.receive<WallBlock>()

                val checked = PositionChecker.checkNewBlockposition(
                    wallBlock.startX,
                    wallBlock.startY,
                    wallBlock.width,
                    wallBlock.height,
                    wallBlockRepository.wallBlocksByMap(wallBlock.mapId),
                    departmentRepository.departmentsByMap(wallBlock.mapId),
                    tillRepository.tillsByMap(wallBlock.mapId),
                    mapRepository.mapById(wallBlock.mapId)!!
                )
                if (!checked) call.respond(HttpStatusCode.BadRequest)


                else if(!RouteCalculation().checkPathExistence( wallBlock.startX,
                        wallBlock.startY,
                        wallBlock.width,
                        wallBlock.height,mapRepository.mapById(wallBlock.mapId)!!,
                        tillRepository.tillsByMap(wallBlock.mapId),
                        wallBlockRepository.wallBlocksByMap(wallBlock.mapId),
                        departmentRepository.departmentsByMap(wallBlock.mapId), -1, wallBlock.id ?: -1)) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid position, there is no path to the tills")
                }
                else {
                    val newWallBlock = wallBlockRepository.updateWallBlock(wallBlock)
                    call.respond(HttpStatusCode.Created, newWallBlock)
                }
            }

            delete("/{wallBlockId}") {
                wallBlockRepository.removeWallBlockById(
                    call.parameters["wallBlockId"]?.toInt() ?: 0
                )
                call.respond(HttpStatusCode.NoContent)
            }

            post {
                try {
                    val wallBlock = call.receive<WallBlock>()
                    val checked = PositionChecker.checkNewBlockposition(
                        wallBlock.startX,
                        wallBlock.startY,
                        wallBlock.width,
                        wallBlock.height,
                        wallBlockRepository.wallBlocksByMap(wallBlock.mapId),
                        departmentRepository.departmentsByMap(wallBlock.mapId),
                        tillRepository.tillsByMap(wallBlock.mapId),
                        mapRepository.mapById(wallBlock.mapId)!!
                    )
                    if (!checked) call.respond(HttpStatusCode.BadRequest, "Invalid position")

                    else if(!RouteCalculation().checkPathExistence( wallBlock.startX,
                            wallBlock.startY,
                            wallBlock.width,
                            wallBlock.height,mapRepository.mapById(wallBlock.mapId)!!,
                            tillRepository.tillsByMap(wallBlock.mapId),
                            wallBlockRepository.wallBlocksByMap(wallBlock.mapId),
                            departmentRepository.departmentsByMap(wallBlock.mapId), -1, wallBlock.id ?: -1)) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid position, there is no path to the tills")
                    }
                    else {
                        val newWallBlock = wallBlockRepository.addWallBlock(wallBlock)
                        call.respond(HttpStatusCode.Created, newWallBlock)
                    }
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
        route("/tills") {
            get("/{tillId}") {
                val tills = tillRepository.tillsByMap(call.parameters["tillId"]?.toInt() ?: 0)
                call.respond(tills)
            }

            delete("/{tillId}") {
                tillRepository.removeTillById(call.parameters["tillId"]?.toInt() ?: 0)
                call.respond(HttpStatusCode.NoContent)
            }

            put("/{id}") {
                val till = call.receive<Till>()

                val checked = PositionChecker.checkNewBlockposition(
                    till.startX,
                    till.startY,
                    till.width,
                    till.height,
                    wallBlockRepository.wallBlocksByMap(till.mapId),
                    departmentRepository.departmentsByMap(till.mapId),
                    tillRepository.tillsByMap(till.mapId),
                    mapRepository.mapById(till.mapId)!!
                )
                if (!checked) call.respond(HttpStatusCode.BadRequest, "Invalid position")
                else {
                    val newTill=tillRepository.updateTill(till)
                    call.respond(HttpStatusCode.Created, newTill)
                }

            }

            post {
                try {
                    val till = call.receive<Till>()
                    val checked = PositionChecker.checkNewBlockposition(
                        till.startX,
                        till.startY,
                        till.width,
                        till.height,
                        wallBlockRepository.wallBlocksByMap(till.mapId),
                        departmentRepository.departmentsByMap(till.mapId),
                        tillRepository.tillsByMap(till.mapId),
                        mapRepository.mapById(till.mapId)!!
                    )
                    if (!checked) call.respond(HttpStatusCode.BadRequest, "Invalid position")
                    else {
                        val newTill = tillRepository.addTill(till)
                        call.respond(HttpStatusCode.Created, newTill)
                    }

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
                    val newStore = storeRepository.addStore(map)
                    call.respond(HttpStatusCode.Created, newStore)

                } catch (e: Exception) {
                    println("Serialization error: ${e.message}") // Log the serialization error
                    call.respond(HttpStatusCode.BadRequest, "Invalid input data: ${e.message}")
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }


    }

}
