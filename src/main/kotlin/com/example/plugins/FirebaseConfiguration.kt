package com.example.plugins

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

object FirebaseConfiguration {

    fun initialize() {
        println(
            "Auth emulator: ${
                System.getenv("FIREBASE_AUTH_EMULATOR_HOST")
            }"
        )
        val stream =
            FirebaseConfiguration::class.java
                .getResourceAsStream("/shopmap-1afca-firebase-adminsdk-fbsvc-2473bb1dca.json")
                ?: error("firebase-service-account.json not found")

        val options = FirebaseOptions.builder()
            .setCredentials(
                GoogleCredentials.fromStream(stream)
            )
            .build()

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
        }
    }
}