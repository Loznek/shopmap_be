package com.example.plugins


import com.example.wallblocks.WallBlockController

import com.example.shoppingList.ShoppingListController
import com.example.departments.DepartmentController
import com.example.departments.departmentRoutes
import com.example.maps.MapController
import com.example.maps.mapRoutes
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
import com.example.shoppingList.shoppingListRoutes
import com.example.stores.StoreController
import com.example.stores.storeRoutes


import com.example.tills.TillController
import com.example.tills.tillRoutes
import com.example.users.UserController
import com.example.users.userRoutes
import com.example.wallblocks.wallBlockRoutes
import io.ktor.http.ContentType

import io.ktor.server.application.*

import io.ktor.server.routing.*

import io.ktor.openapi.OpenApiInfo
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.request.receive

import io.ktor.server.routing.openapi.OpenApiDocSource
import org.koin.ktor.ext.inject


fun Application.configureRouting() {

    val departmentController by inject<DepartmentController>()
    val wallBlockController by inject<WallBlockController>()
    val mapController by inject<MapController>()
    val tillController by inject<TillController>()
    val storeController by inject<StoreController>()
    val recipeController by inject<RecipeController>()
    val navigationController by inject<NavigationController>()
    val ocrController by inject<OcrController>()
    val salesController by inject<SalesController>()
    val productController by inject<ProductController>()
    val userController by inject<UserController>()
    val shoppingListController by inject<ShoppingListController>()




    routing {

        openAPI(path = "openapi") {
            info = OpenApiInfo(
                title = "Store Route Optimization API",
                version = "1.0.0",
                description =
                    """
            API for demonstrating:
            - Shopping list management
            - OCR product recognition
            - Route optimization algorithms
            - Google Places integration
            
            Recommended demo flow:
            1. Login
            2. OCR
            3. Route generation
            """)
            source = OpenApiDocSource.Routing {
                routingRoot.descendants()
            }
        }

        swaggerUI(path = "swagger-ui", swaggerFile = "com_example_ktor_db_app-openapi_6.yaml")

        shoppingListRoutes(shoppingListController)
        userRoutes(userController)

        departmentRoutes(departmentController)
        mapRoutes(mapController)

        wallBlockRoutes(wallBlockController)
        tillRoutes(tillController)

        storeRoutes(storeController)
        salesRoutes(salesController)

        productRoutes(productController)
        navigationRoutes(navigationController)

        recipeRoutes(recipeController)
        ocrRoutes(ocrController)

    }

}


