package com.example.di.modules


import WallBlockController
import com.example.controller.ShoppingListController
import com.example.departments.DepartmentController
import com.example.maps.MapController
import com.example.navigation.NavigationController
import com.example.ocr.OcrController
import com.example.products.ProductController
import com.example.recipes.RecipeController
import com.example.sales.SalesController
import com.example.stores.StoreController
import com.example.tills.TillController
import com.example.users.UserController
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val controllerModule = module {

    singleOf(::DepartmentController)

    singleOf(::WallBlockController)

    singleOf(::MapController)

    singleOf(::TillController)

    singleOf(::StoreController)

    singleOf(::RecipeController)

    singleOf(::NavigationController)

    singleOf(::OcrController)

    singleOf(::SalesController)

    singleOf(::ProductController)

    singleOf(::UserController)

    singleOf(::ShoppingListController)
}