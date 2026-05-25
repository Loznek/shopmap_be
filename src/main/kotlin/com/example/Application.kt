package com.example


import com.example.di.configureKoin

import com.example.plugins.*

import configureAuthentication

import io.ktor.server.application.*
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}
fun Application.module() {
/*
    val departmentRepository = PostgresDepartmentRepository()
    val mapRepository = PostgresMapRepository()
    val storeRepository = PostgresStoreRepository()
    val wallBlockRepository = PostgresWallBlockRepository()
    val tillRepository = PostgresTillRepository()
    val productRepository = PostgresProductRepository()
    val googleMapsInfoRepository = PostgresGoogleMapsInfoRepository()
    val openingHoursRepository = PostgresOpeningHoursRepository()
    val storePictureRepository = PostgresStorePictureRepository()
    val userRepository = PostgresUserRepository()
    val shoppingListRepository =
        PostgresShoppingListRepository()

    val shoppingListItemRepository =
        PostgresShoppingListItemRepository()





    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    val gridBuilder = GridBuilder()
    val pathFinder = PathFinder()
    val pathValidator = PathValidator(pathFinder)

    val departmentService = DepartmentService(
        departmentRepository,
        wallBlockRepository,
        tillRepository,
        mapRepository,
        gridBuilder,
        pathValidator
    )

    val departmentController = DepartmentController(departmentService)

    val wallBlockService = WallBlockService(
        wallBlockRepository,
        departmentRepository,
        tillRepository,
        mapRepository,
        gridBuilder,
        pathValidator
    )

    val wallBlockController = WallBlockController(wallBlockService)
    val pythonMapProcessorClient = PythonMapProcessorClient(httpClient)
    val mapService = MapService(
        mapRepository,
        wallBlockRepository,
        departmentRepository,
        tillRepository,
        pythonMapProcessorClient

    )

    val mapController = MapController(mapService)

    val tillService = TillService(
        tillRepository,
        wallBlockRepository,
        departmentRepository,
        mapRepository
    )

    val tillController = TillController(tillService)


    val storeService = StoreService(
        storeRepository = storeRepository,
        mapRepository = mapRepository,
        departmentRepository = departmentRepository,
        wallBlockRepository = wallBlockRepository,
        tillRepository = tillRepository,
        shoppingListItemRepository = shoppingListItemRepository,
        productRepository = productRepository,
    )



    val recipeService = RecipeService()
    val recipeController = RecipeController(recipeService)

    val navigationService = NavigationService(
        mapRepository = mapRepository,
        departmentRepository = departmentRepository,
        tillRepository = tillRepository,
        wallBlockRepository = wallBlockRepository,
        productRepository = productRepository,
    )

    val navigationController = NavigationController(navigationService)

    val tesseractProvider = TesseractOcrProvider()
    val googleProvider = GoogleDocumentAiProvider()
    val shoppingListParser = ShoppingListParser()

    val ocrService = OcrService(
        tesseractProvider,
        googleProvider,
        shoppingListParser
    )



    val googleApiKey = "YOUR_GOOGLE_API_KEY"

    val googlePlacesClient = GooglePlacesClient(
        client = httpClient,
        apiKey = googleApiKey
    )

    val photoDownloader = PhotoDownloader(
        client = httpClient,
        apiKey = googleApiKey
    )

    val storeGooglePlacesService = StoreGooglePlacesService(
        storeRepository = storeRepository,
        googleMapsInfoRepository = googleMapsInfoRepository,
        openingHoursRepository = openingHoursRepository,
        pictureRepository = storePictureRepository,
        googlePlacesClient = googlePlacesClient,
        photoDownloader = photoDownloader
    )

    val storeController = StoreController(
        service = storeService,
        storeGooglePlacesService = storeGooglePlacesService
    )


    val ocrController = OcrController(ocrService)

    val flyerScraper = FlyerScraper(httpClient)
    val productParser = ProductParser()

    val salesService = SalesService(
        flyerScraper = flyerScraper,
        productParser = productParser
    )

    val shoppingListService =
        ShoppingListService(
            shoppingListRepository,
            shoppingListItemRepository,
            userRepository
        )

    val shoppingListController =
        ShoppingListController(
            shoppingListService
        )


    val productService = ProductService(productRepository)

    // Controllers
    val productController = ProductController(productService)
    val appUserService = UserService(userRepository)
    val appUserController= UserController(appUserService)
    val salesController = SalesController(salesService)
    */

    val googleApiKey =
        environment.config.property("google.apiKey").getString()

    FirebaseConfiguration.initialize()

    configureKoin()

    configureAuthentication()
    configureSerialization()
    configureDatabases()
    configureRouting()
}

