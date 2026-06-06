package com.example.tills

import com.example.exception.ComputationException
import com.example.exception.NotFoundException
import com.example.exception.ValidationException
import com.example.geometry.SpatialValidator
import com.example.model.entity.Department
import com.example.model.entity.Till
import com.example.model.entity.toRect
import com.example.model.repository.DepartmentRepository
import com.example.model.repository.MapRepository
import com.example.model.repository.TillRepository
import com.example.model.repository.WallBlockRepository
import com.example.navigation.GridBuilder
import com.example.navigation.PathFinder
import com.example.navigation.PathValidator
import com.example.navigation.toGrid

class TillService(
    private val tillRepository: TillRepository,
    private val wallBlockRepository: WallBlockRepository,
    private val departmentRepository: DepartmentRepository,
    private val mapRepository: MapRepository,
    private val gridBuilder: GridBuilder,
    private val pathFinder: PathFinder
) {

    suspend fun get(id: Int): Till {
        return tillRepository.tillById(id) ?: throw NotFoundException(
            "Till $id not found"
        )
    }

    suspend fun getByMap(mapId: Int): List<Till> {
        mapRepository.mapById(mapId)
            ?: throw NotFoundException(
                "Map $mapId not found"
            )
        return tillRepository.tillsByMap(mapId)
    }

    suspend fun delete(id: Int) {
        val deleted = tillRepository.removeTillById(id)
        if(!deleted) {
            throw NotFoundException(
                "Till $id not found"
            )
        }
    }

    suspend fun create(till: Till): Till {

        val map = mapRepository.mapById(till.mapId)
            ?: throw NotFoundException("Map not found")
        val wallBlocks = wallBlockRepository.wallBlocksByMap(till.mapId)
        val departments = departmentRepository.departmentsByMap(till.mapId)
        val tills = tillRepository.tillsByMap(till.mapId)
        val obstacles = wallBlocks.map { it.toRect()} + departments.map { it.toRect()} + tills.map { it.toRect()}
        val rect = till.toRect()

        val isValid = SpatialValidator.isValidPosition(
            rect,
            map,
          obstacles
        )

        if (!isValid) {
            throw ValidationException("Invalid till position")
        }

        return tillRepository.addTill(till)
    }

    suspend fun update(till: Till): Till {

        val map = mapRepository.mapById(till.mapId)
            ?: throw NotFoundException("Map not found")

        val wallBlocks = wallBlockRepository
            .wallBlocksByMap(till.mapId)

        val departments = departmentRepository
            .departmentsByMap(till.mapId)

        val obstacles = wallBlocks.map { it.toRect()} + departments.map { it.toRect()}

        val rect = till.toRect()

        val isValid = SpatialValidator.isValidPosition(
            rect,
            map,
            obstacles
        )

        if (!isValid) {
            throw ValidationException("Invalid till position")
        }

        val walkablePoints = gridBuilder.buildWalkablePoints(
            map = map,
            wallBlocks = wallBlocks,
            departments = departments,
            tills = List(1) { till }, // Exclude tills for pathfinding
            newRect = rect,
            excludeDepartmentId = -1,
            excludeWallId=-1
        )

        val entrance =
            map.entranceX.toGrid() to
                    map.entranceY.toGrid()

        val tillPoint =
            (till.startX + till.width / 2).toGrid() to
                    (till.startY + till.height / 2).toGrid()

        val isReachable = pathFinder.dfsPathExist(
            walkablePoints,
            entrance,
            tillPoint
        )

        if (!isReachable) {
            throw ComputationException("The new till position is not reachable from the entrance")
        }


        return tillRepository.updateTill(till)
    }
}