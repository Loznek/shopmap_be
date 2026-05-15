package com.example.ocr.providers

import com.google.cloud.documentai.v1.DocumentProcessorServiceClient
import com.google.cloud.documentai.v1.DocumentProcessorServiceSettings
import com.google.cloud.documentai.v1.ProcessRequest
import com.google.cloud.documentai.v1.RawDocument
import com.google.protobuf.ByteString

class GoogleDocumentAiProvider {

    private val projectId = "169779529407"
    private val location = "eu"
    private val processorId = "57560456921230e3"

    fun extractText(fileBytes: ByteArray): String {
        val processorName =
            "projects/$projectId/locations/$location/processors/$processorId"

        val endpoint = "$location-documentai.googleapis.com:443"

        val settings = DocumentProcessorServiceSettings.newBuilder()
            .setEndpoint(endpoint)
            .build()

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