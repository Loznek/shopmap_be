package com.example.plugins

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.FileInputStream

object FirebaseConfiguration {

    fun initialize() {
        println(
            "Auth emulator: ${
                System.getenv("FIREBASE_AUTH_EMULATOR_HOST")
            }"
        )

        /*


        val credentialsFile =
            System.getenv("FIREBASE_CREDENTIALS_FILE")
                ?: error("FIREBASE_CREDENTIALS_FILE is missing")

        val stream =
            FirebaseConfiguration::class.java
                .getResourceAsStream(credentialsFile)
                ?: error("firebase-service-account.json not found")
        */

        val credentialsFile =
            System.getenv("FIREBASE_CREDENTIALS_FILE")
                ?: error("FIREBASE_CREDENTIALS_FILE is missing")

        val stream =
            FileInputStream(credentialsFile)

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