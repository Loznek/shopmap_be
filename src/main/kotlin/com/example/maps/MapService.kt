package com.example.maps

import com.example.departments.dto.DepartmentResponse
import com.example.model.repository.DepartmentRepository
import com.example.model.repository.MapRepository
import com.example.model.repository.TillRepository
import com.example.model.repository.WallBlockRepository
import com.example.model.entity.Map
import com.example.geometry.SpatialValidator
import com.example.maps.PythonMapProcessorClient
import com.example.maps.dto.ProcessImageRequest
import com.example.model.entity.Department
import com.example.model.entity.toRect
import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class MapService(
    private val mapRepository: MapRepository,
    private val wallBlockRepository: WallBlockRepository,
    private val departmentRepository: DepartmentRepository,
    private val tillRepository: TillRepository,
    private val pythonMapProcessorClient: PythonMapProcessorClient
) {

    suspend fun getById(id: Int): Map {
        return mapRepository.mapById(id)
            ?: throw IllegalArgumentException("Map not found")
    }

    suspend fun delete(id: Int) {
        val map = mapRepository.mapById(id)
            ?: throw IllegalArgumentException("Map not found")

        mapRepository.removeMap(map)
    }

    suspend fun create(map: Map): Map {
        return mapRepository.addMap(map)
    }

    suspend fun update(map: Map): Map {

        val existingMap = mapRepository.mapById(map.id!!)
            ?: throw IllegalArgumentException("Map not found")

        val wallBlocks = wallBlockRepository.wallBlocksByMap(map.id!!)
        val departments = departmentRepository.departmentsByMap(map.id!!)
        val tills = tillRepository.tillsByMap(map.id!!)

        val allRects =
            wallBlocks.map { it.toRect() } + departments.map { it.toRect() } + tills.map { it.toRect() }

        val canResize = SpatialValidator.canResizeMap(
            map.width,
            map.height,
            allRects
        )

        if (!canResize) {
            throw IllegalArgumentException("Map resizing would cause collision")
        }

        return mapRepository.updateMap(map)
    }


    suspend fun processImage(
        imageBytes: ByteArray,
        request: ProcessImageRequest
    ): List<DepartmentResponse> {
        println("Processing image with width: ${request.mapWidth}, height: ${request.mapHeight}")
        val pythonResponse =
            pythonMapProcessorClient.processImage(
                imageBytes = imageBytes,
                mapWidth = request.mapWidth,
                mapHeight = request.mapHeight
            )

        if (pythonResponse.boxes.isEmpty()) {
            throw IllegalArgumentException(
                "No boxes detected"
            )
        }
        val departments = pythonResponse.boxes
            .map { box ->
                DepartmentResponse(
                    id = null,
                    name = box.name,
                    width = box.width,
                    height = box.height,
                    startX = box.startX,
                    startY = box.startY,
                    mapId = request.mapId
                )
            }
        generateDebugFloorPlan( departments, request.mapWidth, request.mapHeight )
        return pythonResponse.boxes
                .map { box ->
                    DepartmentResponse(
                        id = null,
                        name = box.name,
                        width = box.width,
                        height = box.height,
                        startX = box.startX,
                        startY = box.startY,
                        mapId = request.mapId
                    )
                }
    }
    fun generateDebugFloorPlan(
        departments: List<DepartmentResponse>,
        mapWidth: Int,
        mapHeight: Int
    ) {

        val scale = 20

        val widthPx = mapWidth * scale
        val heightPx = mapHeight * scale

        val image = BufferedImage(
            widthPx,
            heightPx,
            BufferedImage.TYPE_INT_RGB
        )

        val g = image.createGraphics()

        // háttér
        g.color = Color.WHITE
        g.fillRect(0, 0, widthPx, heightPx)

        g.font = Font(
            "Arial",
            Font.BOLD,
            14
        )

        departments.forEach { department ->

            val x =
                (department.startX * scale).toInt()

            val y =
                (department.startY * scale).toInt()

            val w =
                (department.width * scale).toInt()

            val h =
                (department.height * scale).toInt()

            // rectangle
            g.color = Color.BLACK

            g.drawRect(
                x,
                y,
                w,
                h
            )

            // text
            g.color = Color.BLUE

            g.drawString(
                department.name,
                x + 5,
                y + 20
            )
        }

        g.dispose()

        ImageIO.write(
            image,
            "png",
            File("debug_floorplan.png")
        )
    }
}







