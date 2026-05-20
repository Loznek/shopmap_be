package com.example.plugins


import ProcessImageResponse
import PythonRequest
import WallBlockController
import com.example.DTO.*
import com.example.departments.DepartmentController
import com.example.departments.departmentRoutes
import com.example.maps.MapController
import com.example.maps.dto.ProcessImageRequest
import com.example.maps.mapRoutes
import com.example.model.entity.*
import com.example.model.repository.*
import com.example.navigation.NavigationController
import com.example.navigation.navigationRoutes
import com.example.ocr.OcrController
import com.example.ocr.ocrRoutes
import com.example.products.ProductController
import com.example.products.productRoutes
import com.example.recipes.RecipeController
import com.example.recipes.recipeRoutes
import com.example.sales.SalesController
import com.example.sales.salesRoutes
import com.example.stores.StoreController
import com.example.stores.storeRoutes
import com.example.tills.TillController
import com.example.tills.tillRoutes
import com.example.wallblocks.wallBlockRoutes
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*

import io.ktor.server.request.*
import kotlinx.serialization.json.Json

import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import kotlin.collections.Map

import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*


fun Application.configureRouting(
    departmentController: DepartmentController,
    wallBlockController: WallBlockController,
    mapController: MapController,
    tillController: TillController,
    storeController: StoreController,
    recipeController: RecipeController,
    navigationController: NavigationController,
    ocrController: OcrController,
    salesController: SalesController,
    productController: ProductController
) {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }
    routing {

        /*
        openAPI(path = "openapi") {
            info = OpenApiInfo("My API", "1.0")
            source = OpenApiDocSource.Routing {
                routingRoot.descendants()
            }
        }*/


        departmentRoutes(departmentController)
        mapRoutes(mapController)

        /*
Blokk mozgatása/létrehozása: belső alg, ami csekoolja, hogy mehet-e oda
Amikor letrejon egy Bolt, annak kell legyen egy bejarata, egy kijarata es egy kasszaja
Az utolsot ezekbol sose lehet torolni
Ezutan utkereso algoritmust kitalalni: melyik lesz ra a jo? A-bol B-be kell menni, es kozben pedig pontokat kell erinteni
*/


        wallBlockRoutes(wallBlockController)
        tillRoutes(tillController)

        /*
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

         */
        storeRoutes(storeController)
        salesRoutes(salesController)

        productRoutes(productController)

        /*
        route("/sales") {
            get {
                val client = HttpClient(CIO)

                val url = "https://akcios-ujsag.hu/akcios-ujsagok/aldi-akcios-ujsag-2026-03-12-03-18/"

                val html = client.get(url).body<String>()

                val doc = Jsoup.parse(html)

                // OCR szöveg
                val rawText = doc.select("p.wp-caption-text").text()

                println("=== RAW TEXT ===")
                println(rawText)

                val items = splitProducts(rawText)

                println("\n=== PARSED ITEMS ===")
                items.forEach {
                    println("----")
                    println(it)
                }

                client.close()
            }
        }
        */

        navigationRoutes(navigationController)

        /*
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
        */
        recipeRoutes(recipeController)
        ocrRoutes(ocrController)

        /*
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
*/
        /*
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

        */


        /*
        post("/api/maps/process-image") {

            val request = call.receive<ProcessImageRequest>()

            val pythonResponse: ProcessImageResponse = client.post("http://localhost:8001/process") {
                contentType(ContentType.Application.Json)

                setBody(
                    PythonRequest(
                        image_path = request.imagePath,
                        map_width = request.mapWidth,
                        map_height = request.mapHeight
                    )
                )
            }.body()

            //call.respond(pythonResponse)

            if (pythonResponse.boxes.isEmpty()) {
                call.respond(HttpStatusCode.BadRequest, "No boxes detected")
                return@post
            }

            // ✅ első box
            val firstBox = pythonResponse.boxes.first()

            val department = Department(
                id = null,
                mapId = request.mapId, // ⚠️ ez kell a requestbe!
                name = firstBox.name,
                width = firstBox.width,
                height = firstBox.height,
                startX = firstBox.startX,
                startY = firstBox.startY
            )

            val newDepartment = departmentRepository.addDepartment(department)

            call.respond(HttpStatusCode.Created, newDepartment)
        }
        */



        /*
        route("/stores/{id}/place-details") {
            get {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid id")

                val store = storeRepository.storeById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound, "Store not found")

                val location = store.location
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Store has no location")
                val storeName = store.name
                val apiKey = "AIzaSyC4nSycI53p7aXzXuJ7soXXZEnHx1HJjo4"

                // 1️⃣ Text search
                val searchResponse: SearchResponse = client.post(
                    "https://places.googleapis.com/v1/places:searchText"
                ) {
                    header("X-Goog-Api-Key", apiKey)
                    header("X-Goog-FieldMask", "places.id,places.displayName,places.formattedAddress")
                    contentType(ContentType.Application.Json)
                    setBody(
                        SearchTextRequest(
                            textQuery = "$storeName $location"
                        )
                    )
                }.body()

                val placeId = searchResponse.places.firstOrNull()?.id
                    ?: return@get call.respond(HttpStatusCode.NotFound, "No place found")

                // 2️⃣ Place details
                val detailsResponse: PlaceDetailsResponse = client.get(
                    "https://places.googleapis.com/v1/places/$placeId"
                ) {
                    header("X-Goog-Api-Key", apiKey)
                    header(
                        "X-Goog-FieldMask",
                        "id,internationalPhoneNumber,websiteUri,googleMapsUri,regularOpeningHours,rating,userRatingCount,priceLevel,photos,accessibilityOptions,parkingOptions"
                    )
                }.body()

                val hasParking = detailsResponse.parkingOptions?.let {
                    (it.freeParkingLot == true) ||
                            (it.freeStreetParking == true) ||
                            (it.freeGarageParking == true)
                } ?: false

                val isWheelchairAccessible =
                    detailsResponse.accessibilityOptions?.wheelchairAccessibleEntrance == true

                val openingHours = detailsResponse.regularOpeningHours?.periods?.map {
                    Triple(
                        it.open.day,
                        "${it.open.hour}:${it.open.minute.toString().padStart(2, '0')}",
                        "${it.close.hour}:${it.close.minute.toString().padStart(2, '0')}"
                    )
                } ?: emptyList()
                val existing = googleMapsInfoRepository.getByStoreId(store.id!!)
                if (existing != null) {
                    return@get call.respond("Already fetched")
                }
                val images = detailsResponse.photos
                    ?.take(3)
                    ?.mapIndexed { index, photo ->
                        downloadPhoto(client, photo.name, store.id!!, index)
                    } ?: emptyList()

                googleMapsInfoRepository.add(
                    GoogleMapsInfo(
                        id = null,
                        storeId = store.id!!,
                        placeId = detailsResponse.id,
                        phoneNumber = detailsResponse.internationalPhoneNumber,
                        websiteUri = detailsResponse.websiteUri,
                        googleMapsUri = detailsResponse.googleMapsUri,
                        rating = detailsResponse.rating,
                        userRatingCount = detailsResponse.userRatingCount,
                        hasParking = hasParking,
                        wheelchairAccessible = isWheelchairAccessible
                    )
                )

                openingHours.forEach { (day, open, close) ->
                    openingHoursRepository.add(
                        OpeningHours(
                            id = null,
                            storeId = store.id,
                            day = day,
                            openTime = open,
                            closeTime = close
                        )
                    )
                }

                images.forEach { path ->
                    pictureRepository.add(
                        StorePicture(
                            id = null,
                            storeId = store.id,
                            path = path
                        )
                    )
                }


                call.respond(detailsResponse)
            }
        }
        */
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


fun splitProducts(text: String): List<String> {
    val cleaned = text.replace(Regex("\\s+"), " ").trim()

    // csak akkor vágunk, ha:
    // - nagybetűs szó
    // - utána vessző (termék neveknél ez tipikus)
    val regex = Regex("(?=\\s[A-ZÁÉÍÓÖŐÚÜŰ]{3,},)")

    return cleaned
        .split(regex)
        .map { it.trim() }
        .filter { it.contains("Ft") } // csak ahol ár van
}

suspend fun downloadPhoto(
    client: HttpClient,
    photoName: String,
    storeId: Int,
    index: Int
): String {

    val url = "https://places.googleapis.com/v1/$photoName/media"
    println(url)
    val response: ByteArray = client.get(url) {
        parameter("maxHeightPx", 800)
        header("X-Goog-Api-Key", "AIzaSyC4nSycI53p7aXzXuJ7soXXZEnHx1HJjo4")
    }.body()

    val dir = File("storePictures/$storeId")
    dir.mkdirs()

    val fileName = "image_$index.jpg"
    val file = File(dir, fileName)

    file.writeBytes(response)

    return file.absolutePath
}
