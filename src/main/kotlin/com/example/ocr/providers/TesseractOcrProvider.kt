package com.example.ocr.providers



import net.sourceforge.tess4j.Tesseract
import net.sourceforge.tess4j.TesseractException
import java.io.File

class TesseractOcrProvider {

    private val tesseract = Tesseract().apply {
        setDatapath("D:\\MSC_Onlab_2\\Tesseract\\tessdata")
        setLanguage("eng")
        setOcrEngineMode(1)
        setPageSegMode(6)
    }

    fun extractText(imageFile: File): String {
        return try {
            tesseract.doOCR(imageFile)
        } catch (e: TesseractException) {
            throw RuntimeException("Error processing image with Tesseract", e)
        }
    }
}