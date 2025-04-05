package com.example.services

import com.google.cloud.documentai.v1.DocumentProcessorServiceClient
import com.google.cloud.documentai.v1.DocumentProcessorServiceSettings
import com.google.cloud.documentai.v1.ProcessRequest
import com.google.cloud.documentai.v1.RawDocument
import net.sourceforge.tess4j.Tesseract
import net.sourceforge.tess4j.TesseractException
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths


object OCRService {
    private val tesseract = Tesseract().apply {
        // Set the path where Tesseract is installed (Windows users must specify this)
        setDatapath("D:\\MSC_Onlab_2\\Tesseract\\tessdata") // Change this for your OS
        setLanguage("eng")
        setOcrEngineMode(1)
        setPageSegMode(6)// Change this to another language if needed (e.g., "jpn", "deu")
    }

    fun extractTextFromImage(imageFile: File): String {
        return try {
            tesseract.doOCR(imageFile)
        } catch (e: TesseractException) {
            throw RuntimeException("Error processing image", e)
        }
    }
    fun preprocessImage(imagePath: String): File {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

        val src = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_GRAYSCALE)
        val dest = Mat()

        // Apply adaptive thresholding
        Imgproc.adaptiveThreshold(src, dest, 255.0, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 11, 2.0)

        val outputFile = File("preprocessed.jpg")
        Imgcodecs.imwrite(outputFile.absolutePath, dest)

        return outputFile
    }




    fun processOcrDocument(fileBytes: ByteArray): String {
        val projectId = "169779529407"
        val location = "eu" // or "eu"
        val processorId = "57560456921230e3"

        val processorName = "projects/$projectId/locations/$location/processors/$processorId"

        val endpoint = "$location-documentai.googleapis.com:443"
        // Create a custom Netty channel with the custom endpoint
        val imageFileData: ByteArray = Files.readAllBytes(Paths.get("C:\\Users\\zolta\\Downloads\\ocrtest2.jpg"))

        val settings =
            DocumentProcessorServiceSettings.newBuilder().setEndpoint(endpoint).build()

        try {
            // Create the DocumentProcessorServiceClient using the custom settings
            DocumentProcessorServiceClient.create(settings).use { client ->
                val document = RawDocument.newBuilder()
                    .setContent(com.google.protobuf.ByteString.copyFrom(fileBytes))
                    .setMimeType("image/jpeg") // Change if processing images
                    .build()

                val request = ProcessRequest.newBuilder()
                    .setName(processorName)
                    .setRawDocument(document)
                    .build()

                val response = client.processDocument(request)
                return response.document.text
            }
        } catch (e: Exception) {
            throw RuntimeException("Error processing document", e)
        }
    }
}