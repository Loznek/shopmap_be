package com.example.di

import com.example.di.modules.appModules
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {

    install(Koin) {

        properties(
            mapOf(
                "google.apiKey" to environment.config
                    .property("google.apiKey")
                    .getString(),

                "openai.apiKey" to environment.config
                    .property("openai.apiKey")
                    .getString()
            )
        )


        modules(appModules)
    }
}