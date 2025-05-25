package com.example.plugins


import com.example.DTO.*
import com.example.algorithms.PositionChecker
import com.example.algorithms.RouteCalculation
import com.example.model.entity.*
import com.example.model.entity.Map
import com.example.model.repository.*
import com.example.services.OCRService
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureRouting(departmentRepository: PostgresDepartmentRepository, mapRepository: PostgresMapRepository, storeRepository: PostgresStoreRepository, wallBlockRepository: PostgresWallBlockRepository, tillRepository: PostgresTillRepository, shelfRepository: PostgresShelfRepository, shoppingListRepository: ShoppingListRepository, shoppingListItemRepository: ShoppingListItemRepository, productRepository: PostgresProductRepository) {
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

        route("/shelves"){
            get("/{departmentId}") {
                val shelves = shelfRepository.shelvesByDepartment(call.parameters["departmentId"]?.toInt() ?: 0)
                call.respond(shelves)

            }

            delete("/{shelfId}") {
                shelfRepository.removeShelfById(call.parameters["shelfId"]?.toInt() ?: 0)
                call.respond(HttpStatusCode.NoContent)
            }

            put("/{id}") {
                val shelf = call.receive<Shelf>()
                val newShelf=shelfRepository.updateShelf(shelf)
                call.respond(HttpStatusCode.Created, newShelf)

            }

            post {
                try {
                    val shelf = call.receive<ShelfCreation>()
                    val newShelf = Shelf(
                        id = 0,
                        departmentId = shelf.departmentId,
                        width = shelf.width,
                        height = shelf.height,
                        startX = shelf.startX,
                        startY = shelf.startY,
                        midx = 0.0,
                        midy = 0.0
                    )
                    //val newShelf = shelfRepository.addShelf(shelf)
                    call.respond(HttpStatusCode.Created, newShelf)
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
        route("/ocr") {
            get() {
                val filePath = call.request.queryParameters["file"]?.replace("/", "\\")
                var extractedText = ""
                val file = File(filePath)
                extractedText = OCRService.extractTextFromImage(file)
                //file.delete() // Clean up uploaded file
                if (extractedText.isNotBlank()) {
                    call.respond(HttpStatusCode.OK, extractedText)
                } else {
                    call.respond(HttpStatusCode.BadRequest, "No text found in image")
                }
            }
        }
        route("googleocr"){
            post() {

                val fileBytes = call.receive<ByteArray>()
                val extractedText = OCRService.processOcrDocument(fileBytes)



                val shoppingList = ShopList(
                items = listOf(
                    ShopItem(name = "Apples", quantity = "5"),
                    ShopItem(name = "Milk", quantity = "2"),
                    ShopItem(name = "Bread", quantity = "1")
                )
            )

                call.respond( HttpStatusCode.OK,shoppingList)
            }
        }
        route("savelist"){
            post() {
                val list = call.receive<ShoppingListWithUserDTO>()
                val shoppingListId= shoppingListRepository.addShoppingList(
                    ShoppingList(
                        id = null, // ID will be generated by the database
                        userId = list.userId,
                        name=list.listName
                    )
                ).id
                for (item in list.items) {
                    shoppingListItemRepository.addShoppingListItem(
                        ShoppingListItem(
                            itemId = null, // ID will be generated by the database
                            shoppingListId = shoppingListId!!, // This should be the ID of the shopping list created above
                            shoppingItemName = item.name,
                            attributes = item.quantity
                        )
                    )
                }
                //Save that list and return the id
                call.respond(HttpStatusCode.OK, shoppingListId?: -1)
            }
        }

        route("concretlist"){
            post() {
                val list = call.receive<ProductsInShop>()
                val products = productRepository.productsByStoreId(list.storeId)
                val shoppingListDB = shoppingListRepository.shoppingListById(list.shoppingListId)
                val shoppingItems = shoppingListItemRepository.itemsByShoppingList(list.shoppingListId)
                //call AI service to get concrete list
                //give back concreteList
                val shoppingList = ConcreteShopList(
                    items = listOf(
                        ConcreteShopItem(
                            name = "Milk",
                            productName = "Whole Milk 1L",
                            productId = 101,
                            quantity = "2"
                        ),
                        ConcreteShopItem(
                            name = "Bread",
                            productName = "Whole Wheat Bread",
                            productId = 102,
                            quantity = "1"
                        ),
                        ConcreteShopItem(
                            name = "Eggs",
                            productName = null,  // e.g. unlinked product
                            productId = null,
                            quantity = "12"
                        )
                    )
                )
                call.respond(HttpStatusCode.OK, shoppingList)
            }
        }
        route("routeplan"){
            post("/{userId}") {
                val concreteData = call.receive<SimpleRoutePlanning>()

                val planning: RoutePlan = RoutePlan(
                    listOf(
                        Pair(1, 2),
                        Pair(3, 4),
                        Pair(5, 6)
                    )
                )
                call.respond(HttpStatusCode.OK, planning)

            }
        }

    }


}
