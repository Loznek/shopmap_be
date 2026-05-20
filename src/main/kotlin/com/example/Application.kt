package com.example

import WallBlockController
import com.example.departments.DepartmentController
import com.example.departments.DepartmentService
import com.example.dto.MapService
import com.example.maps.MapController
import com.example.maps.PythonMapProcessorClient
import com.example.model.repository.*
import com.example.navigation.*
import com.example.ocr.OcrController
import com.example.ocr.OcrService
import com.example.ocr.parser.ShoppingListParser
import com.example.ocr.providers.GoogleDocumentAiProvider
import com.example.ocr.providers.TesseractOcrProvider
import com.example.plugins.*
import com.example.products.ProductController
import com.example.products.ProductService
import com.example.recipes.RecipeController
import com.example.recipes.RecipeService
import com.example.sales.FlyerScraper
import com.example.sales.ProductParser
import com.example.sales.SalesController
import com.example.sales.SalesService
import com.example.stores.*
import com.example.tills.TillController
import com.example.tills.TillService
import com.example.wallblocks.WallBlockService
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}
fun Application.module() {

    val departmentRepository = PostgresDepartmentRepository()
    val mapRepository = PostgresMapRepository()
    val storeRepository = PostgresStoreRepository()
    val wallBlockRepository = PostgresWallBlockRepository()
    val tillRepository = PostgresTillRepository()
    val shelfRepository = PostgresShelfRepository()
    val shoppingListRepository = PostgresShoppingListRepository()
    val shoppingListItemRepository = PostgresShoppingListItemRepository()
    val productRepository = PostgresProductRepository()
    val googleMapsInfoRepository = PostgresGoogleMapsInfoRepository()
    val openingHoursRepository = PostgresOpeningHoursRepository()
    val storePictureRepository = PostgresStorePictureRepository()


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
        tillRepository = tillRepository
    )



    val recipeService = RecipeService()
    val recipeController = RecipeController(recipeService)

    val navigationService = NavigationService(
        mapRepository = mapRepository,
        departmentRepository = departmentRepository,
        tillRepository = tillRepository,
        wallBlockRepository = wallBlockRepository
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


    val productService = ProductService(productRepository)

    // Controllers
    val productController = ProductController(productService)

    val salesController = SalesController(salesService)
    configureSerialization()
    configureDatabases()
    configureRouting( departmentController, wallBlockController, mapController, tillController, storeController, recipeController, navigationController, ocrController, salesController, productController)
}

