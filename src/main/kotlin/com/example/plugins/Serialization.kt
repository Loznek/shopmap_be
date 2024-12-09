package com.example.plugins


import com.example.DTO.RoutePlanning
import com.example.algorithms.PositionChecker
import com.example.algorithms.RouteCalculation
import com.example.model.entity.*
import com.example.model.entity.Map
import com.example.model.repository.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException

fun Application.configureSerialization() {

    install(ContentNegotiation) {
        json()
    }
    }
