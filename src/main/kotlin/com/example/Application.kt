package com.example


import com.example.di.configureKoin

import com.example.plugins.*
import com.example.plugins.configureAuthentication
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod

import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.CORS

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}
fun Application.module() {

    FirebaseConfiguration.initialize()
    configureCors()
    configureExceptionHandling()
    configureKoin()
    configureAuthentication()
    configureSerialization()
    configureDatabases()
    configureRouting()
}

