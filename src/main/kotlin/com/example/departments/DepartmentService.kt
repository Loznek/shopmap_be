package com.example.departments

import com.example.departments.dto.CreateDepartmentRequest
import com.example.departments.dto.UpdateDepartmentRequest
import com.example.departments.dto.toEntity
import com.example.geometry.SpatialValidator
import com.example.model.entity.Department
import com.example.model.entity.toRect
import com.example.model.repository.DepartmentRepository
import com.example.model.repository.MapRepository
import com.example.model.repository.TillRepository
import com.example.model.repository.WallBlockRepository
import com.example.navigation.GridBuilder
import com.example.navigation.PathValidator

class DepartmentService(
    private val departmentRepository: DepartmentRepository,
    private val wallBlockRepository: WallBlockRepository,
    private val tillRepository: TillRepository,
    private val mapRepository: MapRepository,
    private val gridBuilder: GridBuilder,
    private val pathValidator: PathValidator
) {

    suspend fun getByMap(mapId: Int): List<Department> {
        return departmentRepository.departmentsByMap(mapId)
    }

    suspend fun delete(departmentId: Int) {
        departmentRepository.removeDepartmentById(departmentId)
    }

    suspend fun create(request: CreateDepartmentRequest): Department {
        val department = request.toEntity()

        val map = mapRepository.mapById(department.mapId)
            ?: throw IllegalArgumentException("Map not found")

        val rect = department.toRect()

        val obstacles =
            wallBlockRepository.wallBlocksByMap(department.mapId).map { it.toRect() } +
                    departmentRepository.departmentsByMap(department.mapId).map { it.toRect() } +
                    tillRepository.tillsByMap(department.mapId).map { it.toRect() }

        if (!SpatialValidator.isValidPosition(rect, map, obstacles)) {
            throw IllegalArgumentException("Invalid position")
        }

        return departmentRepository.addDepartment(department)
    }

    suspend fun update(request: UpdateDepartmentRequest): Department {
        val department = request.toEntity()

        val map = mapRepository.mapById(department.mapId)
            ?: throw IllegalArgumentException("Map not found")

        val rect = department.toRect()

        val wallBlocks = wallBlockRepository
            .wallBlocksByMap(department.mapId)


        val departments = departmentRepository
            .departmentsByMap(department.mapId)
            .filter { it.id != department.id } // IMPORTANT for update


        val tills = tillRepository
            .tillsByMap(department.mapId)


        val obstacles = wallBlocks.map { it.toRect()} + departments.map { it.toRect()} + tills.map { it.toRect()}


        if (!SpatialValidator.isValidPosition(rect, map, obstacles)) {
            throw IllegalArgumentException("Invalid position")
        }



        val walkablePoints = gridBuilder.buildWalkablePoints(
            map = map,
            wallBlocks = wallBlocks,
            departments = departments,
            tills = tills,
            newRect = rect,
            excludeDepartmentId = department.id!!,
            excludeWallId=-1
        )

        val isReachable = pathValidator.pathExists(
            walkablePoints,
            map.entranceX.toInt() to map.entranceY.toInt(),
            tills[0].startX.toInt() to tills[0].startY.toInt()
        )

        if (!isReachable) {
            throw IllegalArgumentException("No path to tills")
        }

        return departmentRepository.updateDepartment(department)
    }
}