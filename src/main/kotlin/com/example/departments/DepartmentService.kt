package com.example.departments

import com.example.departments.dto.CreateDepartmentRequest
import com.example.departments.dto.UpdateDepartmentRequest
import com.example.departments.dto.toEntity
import com.example.exception.ComputationException
import com.example.exception.NotFoundException
import com.example.exception.ValidationException
import com.example.geometry.SpatialValidator
import com.example.model.entity.Department
import com.example.model.entity.toRect
import com.example.model.repository.DepartmentRepository
import com.example.model.repository.MapRepository
import com.example.model.repository.TillRepository
import com.example.model.repository.WallBlockRepository
import com.example.navigation.GridBuilder
import com.example.navigation.PathFinder
import com.example.navigation.PathValidator
import com.example.navigation.toGrid

class DepartmentService(
    private val departmentRepository: DepartmentRepository,
    private val wallBlockRepository: WallBlockRepository,
    private val tillRepository: TillRepository,
    private val mapRepository: MapRepository,
    private val gridBuilder: GridBuilder,
    private val pathFinder: PathFinder
) {
    suspend fun get(id: Int): Department {
            return departmentRepository
                .departmentById(id)
                ?: throw NotFoundException(
                    "Department $id not found"
                )

    }

    suspend fun getByMap(mapId: Int): List<Department> {
        mapRepository.mapById(mapId)
            ?: throw NotFoundException(
                "Map $mapId not found"
            )
        return departmentRepository.departmentsByMap(mapId)
    }

    suspend fun delete(departmentId: Int) {
        val deleted =
            departmentRepository.removeDepartmentById(
                departmentId
            )
        if (!deleted) {
            throw NotFoundException(
                "Department $departmentId not found"
            )
        }

    }

    suspend fun create(request: CreateDepartmentRequest): Department {
        val department = request.toEntity()

        val map = mapRepository.mapById(department.mapId)
            ?: throw NotFoundException("Map not found")

        val rect = department.toRect()

        val wallBlocks = wallBlockRepository
            .wallBlocksByMap(department.mapId)


        val departments = departmentRepository
            .departmentsByMap(department.mapId)

        val tills = tillRepository
            .tillsByMap(department.mapId)

        val obstacles = wallBlocks.map { it.toRect()} + departments.map { it.toRect()} + tills.map { it.toRect()}

        if (!SpatialValidator.isValidPosition(rect, map, obstacles)) {
            throw ValidationException("Invalid department position")
        }

        val walkablePoints = gridBuilder.buildWalkablePoints(
            map = map,
            wallBlocks = wallBlocks,
            departments = departments,
            tills = tills,
            newRect = rect,
            excludeDepartmentId = -1,
            excludeWallId=-1
        )

        val entrance =
            map.entranceX.toGrid() to
                    map.entranceY.toGrid()

        val tillPoint =
            (tills[0].startX + tills[0].width / 2).toGrid() to
                    (tills[0].startY + tills[0].height / 2).toGrid()

        val isReachable = pathFinder.dfsPathExist(
            walkablePoints,
            entrance,
            tillPoint
        )

        if (!isReachable) {
            throw ComputationException("Department blocks access to tills")
        }

        return departmentRepository.addDepartment(department)
    }

    suspend fun update(request: UpdateDepartmentRequest): Department {
        val department = request.toEntity()

        val existing =
            departmentRepository
                .departmentById(department.id!!)
                ?: throw NotFoundException(
                    "Department ${department.id} not found"
                )

        val positionChanged =
            existing.startX != department.startX ||
                    existing.startY != department.startY ||
                    existing.width != department.width ||
                    existing.height != department.height

        if (!positionChanged) {

            return departmentRepository
                .updateDepartment(department)
        }

        val map = mapRepository.mapById(department.mapId)
            ?: throw NotFoundException("Map not found")


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
            throw ValidationException("Invalid position for modified department")
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

        val entrance =
            map.entranceX.toGrid() to
                    map.entranceY.toGrid()

        val tillPoint =
            (tills[0].startX + tills[0].width / 2).toGrid() to
                    (tills[0].startY + tills[0].height / 2).toGrid()

        val isReachable = pathFinder.dfsPathExist(
            walkablePoints,
            entrance,
            tillPoint
        )

        if (!isReachable) {
            throw ComputationException("Department blocks access to tills")
        }

        return departmentRepository.updateDepartment(department)
    }
}