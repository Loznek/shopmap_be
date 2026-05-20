package com.example.dto

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


    suspend fun processImage(request: ProcessImageRequest): Department {
        val pythonResponse = pythonMapProcessorClient.processImage(
            imagePath = request.imagePath,
            mapWidth = request.mapWidth,
            mapHeight = request.mapHeight
        )

        if (pythonResponse.boxes.isEmpty()) {
            throw IllegalArgumentException("No boxes detected")
        }

        val firstBox = pythonResponse.boxes.first()

        val department = Department(
            id = null,
            mapId = request.mapId,
            name = firstBox.name,
            width = firstBox.width,
            height = firstBox.height,
            startX = firstBox.startX,
            startY = firstBox.startY
        )

        return departmentRepository.addDepartment(department)
    }
}