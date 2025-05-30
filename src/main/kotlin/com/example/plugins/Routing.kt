package com.example.plugins


import com.example.DTO.*
import com.example.algorithms.PositionChecker
import com.example.algorithms.RouteCalculation
import com.example.model.entity.*
import com.example.model.repository.*
import com.example.services.OCRService
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*

import io.ktor.server.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.JsonElement
import java.io.File
import kotlin.collections.Map
import kotlin.math.floor
import kotlin.math.round

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

                    /*
                    else if(!RouteCalculation().checkPathExistence( department.startX,
                            department.startY,
                            department.width,
                            department.height, mapRepository.mapById(department.mapId)!!,
                            tillRepository.tillsByMap(department.mapId),
                            wallBlockRepository.wallBlocksByMap(department.mapId),
                            departmentRepository.departmentsByMap(department.mapId), department.id ?: -1, -1)) {
                       call.respond(HttpStatusCode.BadRequest, "Invalid position, there is no path to the tills")
                    }*/

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
                val map = call.receive<com.example.model.entity.Map>()
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
                    val map = call.receive<com.example.model.entity.Map>()
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
            get("/{storeId}"){
                val map = mapRepository.mapsByStoreId(call.parameters["departmentId"]?.toInt() ?: 0)
                val departments = departmentRepository.departmentsByMap(map.first().id!!)
                val allShelves = mutableListOf<Shelf>()
                for (dep in departments){
                    allShelves.addAll(shelfRepository.shelvesByDepartment(dep.id!!))
                }
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
                    val (midx, midy) = when (shelf.shelfType) {
                        OuterSide.Left -> Pair(
                            shelf.startX - 1.0,
                            floor(shelf.startY - shelf.height / 2) + 1.0
                        )
                        OuterSide.Right -> Pair(
                            shelf.startX + shelf.width,
                            floor(shelf.startY - shelf.height / 2) + 1.0
                        )
                        OuterSide.Up -> Pair(
                            floor(shelf.startX + shelf.width / 2),
                            shelf.startY + 1.0
                        )
                        OuterSide.Down -> Pair(
                            floor(shelf.startX + shelf.width / 2),
                            shelf.startY - shelf.height
                        )
                    }
                    val newShelf = Shelf(
                        id = null,
                        departmentId = shelf.departmentId,
                        width = shelf.width,
                        height = shelf.height,
                        startX = shelf.startX,
                        startY = shelf.startY,
                        midx = midx,
                        midy = midy
                    )
                    val newestShelf = shelfRepository.addShelf(newShelf)
                    call.respond(HttpStatusCode.Created, newestShelf)
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


                val shoppingItems = parseShoppingList(extractedText)

                val shoppingList = ShopList(
                items = shoppingItems
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
                for (item in list.items.items) {
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
                call.respond(HttpStatusCode.OK, shoppingListId?: -1)//
            }
        }

        route("concretlist"){
            post() {
                val list = call.receive<ProductsInShop>()
                val products = productRepository.productsByStoreId(list.storeId)
                val shoppingListDB = shoppingListRepository.shoppingListById(list.shoppingListId)
                val shoppingItems = shoppingListItemRepository.itemsByShoppingList(list.shoppingListId)

                val storeProducts: List<Pair<Int?, String>>  = products.map { Pair(it.Id, it.name) }
                val items: List<String> = shoppingItems.map { it.shoppingItemName }

                val request: AiRequest = AiRequest(
                    products = storeProducts,
                    listItems = items,
                )

                val client = HttpClient() {
                    install(HttpTimeout) {
                        requestTimeoutMillis = 30_000  // 30 seconds
                        connectTimeoutMillis = 10_000
                        socketTimeoutMillis = 30_000
                    }
                }

                val res = client.post("http://localhost:8090/ask") {
                    contentType(ContentType.Application.Json)
                    setBody(Json.encodeToString(request))
                }


                val responseBody = res.bodyAsText()

                val concreteList = parseShopList(responseBody)
                println(concreteList)
                println("Response from AI service: $responseBody")
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
                call.respond(HttpStatusCode.OK, concreteList)
            }
        }
        route("routeplan"){
            post() {

                val concreteData = call.receive<SimpleRoutePlanning>()
                val map = mapRepository.mapsByStoreId(concreteData.storeId)
                val departments = departmentRepository.departmentsByMap(map.first().id!!)
                val allShelves = mutableListOf<Shelf>()
                for (dep in departments){
                    allShelves.addAll(shelfRepository.shelvesByDepartment(dep.id!!))
                }
                val destinationShelves = mutableListOf<Shelf>()
                for (item in concreteData.concreteShopList.items) {
                    destinationShelves.add(
                       productRepository.productById(item.productId ?: 0)?.let {
                            allShelves.firstOrNull { shelf -> shelf.id == it.shelfId }
                        } ?: throw IllegalArgumentException("Product with ID ${item.productId} not found"))
                }
                val path = RouteCalculation().calculateShortestRoutesWithShelves(
                    map = map.first(),
                    tills = tillRepository.tillsByMap(map.first().id!!),
                    wallBlocks = wallBlockRepository.wallBlocksByMap(map.first().id!!),
                    allShelves = allShelves,
                    destinationShelves = destinationShelves
                )

                val planning: RoutePlan = RoutePlan(path)
                call.respond(HttpStatusCode.OK, planning)

                /*
                val planning: RoutePlan = RoutePlan(
                    listOf(
                        Pair(1, 2),
                        Pair(3, 4),
                        Pair(5, 6)
                    )
                )
                call.respond(HttpStatusCode.OK, planning)
                */

            }
        }
        route("products"){
            post {
                val product = call.receive<Product>()
                val newProduct = productRepository.addProduct(product)
                call.respond(HttpStatusCode.Created, newProduct)
            }
        }

    }




}

fun parseShoppingList(text: String): List<ShopItem> {
    val lines = text.lines().filter { it.isNotBlank() }
    val items = mutableListOf<ShopItem>()

    var i = 0
    while (i < lines.size - 1) {
        val name = lines[i].trim()
        val quantity = lines[i + 1].trim()
        items.add(ShopItem(name, quantity))
        i += 2
    }

    // Handle possible odd number of lines (no quantity)
    if (i == lines.lastIndex) {
        items.add(ShopItem(lines[i].trim(), ""))
    }

    return items
}




fun parseShopList(responseJson: String): ConcreteShopList {
    val json = Json { ignoreUnknownKeys = true }

    val outerMap = json.decodeFromString<Map<String, String>>(responseJson)

    val answerJson = outerMap["answer"]
        ?: throw IllegalArgumentException("Missing 'answer' field")

    val parsedItems = json.decodeFromString<List<ConcreteShopItemTemp>>(answerJson)

    val items = parsedItems.map {
        ConcreteShopItem(
            name = it.list_item_name,
            productId = it.product_id,
            productName = it.product_name,
            quantity = "1"
        )
    }

    return ConcreteShopList(items)
}
