package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*

fun configureDatabases() {

    val dbUrl =
        System.getenv("DB_URL")
            ?: error("DB_URL is missing")

    val dbUser =
        System.getenv("DB_USER")
            ?: error("DB_USER is missing")

    val dbPassword =
        System.getenv("DB_PASSWORD")
            ?: error("DB_PASSWORD is missing")

    Database.connect(
        url = dbUrl,
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword
    )
}


