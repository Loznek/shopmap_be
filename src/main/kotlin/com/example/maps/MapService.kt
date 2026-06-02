package com.example.maps

import com.example.departments.dto.DepartmentResponse
import com.example.exception.NotFoundException
import com.example.exception.ValidationException
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
            ?: throw NotFoundException("Map $id not found")
    }

    suspend fun delete(id: Int) {
        val deleted = mapRepository.removeMap(id)
        if (!deleted) {
            throw NotFoundException("Map $id not found")
        }

    }

    suspend fun create(map: Map): Map {
        if (map.width <= 0 || map.height <= 0) {
            throw ValidationException("Map dimensions must be positive")
        }

        return mapRepository.addMap(map)
    }

    suspend fun update(map: Map): Map {

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
            throw ValidationException("Map resizing would cause collision")
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
            throw NotFoundException(
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
        mapWidth: Double,
        mapHeight: Double
    ) {

        val scale = 40

        val widthPx = mapWidth * scale
        val heightPx = mapHeight * scale

        val image = BufferedImage(
            widthPx.toInt(),
            heightPx.toInt(),
            BufferedImage.TYPE_INT_RGB
        )

        val g = image.createGraphics()

        // háttér
        g.color = Color.WHITE
        g.fillRect(0, 0, widthPx.toInt(), heightPx.toInt())

        g.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        )

        g.font = Font(
            "Arial",
            Font.BOLD,
            30
        )
        g.stroke = BasicStroke(5f)
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







