package com.example.stores



import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import java.io.File

class PhotoDownloader(
    private val client: HttpClient,
    private val apiKey: String
) {

    suspend fun download(
        photoName: String,
        storeId: Int,
        index: Int
    ): String {
        val url = "https://places.googleapis.com/v1/$photoName/media"

        val imageBytes: ByteArray = client.get(url) {
            parameter("maxHeightPx", 800)
            header("X-Goog-Api-Key", apiKey)
        }.body()

        val directory = File("storePictures/$storeId")
        directory.mkdirs()

        val file = File(directory, "image_$index.jpg")
        file.writeBytes(imageBytes)

        return file.absolutePath
    }
}