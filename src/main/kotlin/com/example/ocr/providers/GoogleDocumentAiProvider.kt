package com.example.ocr.providers

import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.documentai.v1.DocumentProcessorServiceClient
import com.google.cloud.documentai.v1.DocumentProcessorServiceSettings
import com.google.cloud.documentai.v1.ProcessRequest
import com.google.cloud.documentai.v1.RawDocument
import com.google.protobuf.ByteString

class GoogleDocumentAiProvider(
    private val credentialsFile: String
) {

    private val projectId = "169779529407"
    private val location = "eu"
    private val processorId = "57560456921230e3"


    private val settings: DocumentProcessorServiceSettings

    init {

        val credentialsStream =
            javaClass.classLoader.getResourceAsStream(credentialsFile)
                ?: throw IllegalStateException(
                    "Credential file not found: $credentialsFile"
                )

        val credentials =
            GoogleCredentials.fromStream(credentialsStream)
                .createScoped(
                    listOf(
                        "https://www.googleapis.com/auth/cloud-platform"
                    )
                )

        credentials.refreshIfExpired()
        println(credentials.accessToken?.tokenValue?.take(20))
        println("Access token: ${credentials.accessToken != null}")

        settings =
            DocumentProcessorServiceSettings.newBuilder()
                .setCredentialsProvider(
                    FixedCredentialsProvider.create(credentials)
                )
                .setEndpoint("$location-documentai.googleapis.com:443")
                .build()


        println("Credential file: $credentialsFile")
        println(javaClass.classLoader.getResource(credentialsFile))

    }



    fun extractText(fileBytes: ByteArray): String {


        val processorName =
            "projects/$projectId/locations/$location/processors/$processorId"
        /*
                val endpoint = "$location-documentai.googleapis.com:443"

                val settings = DocumentProcessorServiceSettings.newBuilder()
                    .setCredentialsProvider(
                        FixedCredentialsProvider.create(credentials)
                    )
                    .setEndpoint(endpoint)
                    .build()
                */
        DocumentProcessorServiceClient.create(settings).use { client ->
            val document = RawDocument.newBuilder()
                .setContent(ByteString.copyFrom(fileBytes))
                .setMimeType("image/jpeg")
                .build()

            val request = ProcessRequest.newBuilder()
                .setName(processorName)
                .setRawDocument(document)
                .build()

            val response = client.processDocument(request)
            return response.document.text
        }
    }
}