package com.example

import com.example.model.repository.*
import com.example.plugins.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

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

    configureSerialization()
    configureDatabases()
    configureRouting(departmentRepository, mapRepository, storeRepository, wallBlockRepository, tillRepository, shelfRepository, shoppingListRepository, shoppingListItemRepository,  productRepository)
}

