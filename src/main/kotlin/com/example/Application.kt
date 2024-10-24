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
    install(ContentNegotiation) {
        json()
    }
    val departmentRepository = PostgresDepartmentRepository()
    val mapRepository = PostgresMapRepository()
    val storeRepository = PostgresStoreRepository()
    val wallBlockRepository = PostgresWallBlockRepository()
    val tillRepository = PostgresTillRepository()

    configureSerialization(departmentRepository, mapRepository, storeRepository, wallBlockRepository, tillRepository)
    configureDatabases()
    configureRouting()
}

