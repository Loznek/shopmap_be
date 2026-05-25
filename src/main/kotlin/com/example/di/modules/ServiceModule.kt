package com.example.di.modules

import ShoppingListService
import UserService
import com.example.departments.DepartmentService
import com.example.maps.MapService
import com.example.navigation.NavigationService
import com.example.ocr.OcrService
import com.example.products.ProductService
import com.example.recipes.RecipeService
import com.example.sales.SalesService
import com.example.stores.StoreGooglePlacesService
import com.example.stores.StoreService
import com.example.tills.TillService
import com.example.wallblocks.WallBlockService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val serviceModule = module {

    singleOf(::DepartmentService)

    singleOf(::WallBlockService)

    singleOf(::MapService)

    singleOf(::TillService)

    singleOf(::StoreService)

    single {

        RecipeService(
            client = get(),
            apiKey = getProperty("openai.apiKey")
        )
    }

    singleOf(::NavigationService)

    singleOf(::OcrService)

    singleOf(::SalesService)

    singleOf(::ShoppingListService)

    singleOf(::ProductService)

    singleOf(::UserService)

    singleOf(::StoreGooglePlacesService)
}