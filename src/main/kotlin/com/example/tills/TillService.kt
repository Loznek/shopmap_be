package com.example.tills

import com.example.geometry.SpatialValidator
import com.example.model.entity.Till
import com.example.model.entity.toRect
import com.example.model.repository.DepartmentRepository
import com.example.model.repository.MapRepository
import com.example.model.repository.TillRepository
import com.example.model.repository.WallBlockRepository

class TillService(
    private val tillRepository: TillRepository,
    private val wallBlockRepository: WallBlockRepository,
    private val departmentRepository: DepartmentRepository,
    private val mapRepository: MapRepository
) {

    suspend fun getByMap(mapId: Int): List<Till> {
        return tillRepository.tillsByMap(mapId)
    }

    suspend fun delete(id: Int) {
        tillRepository.removeTillById(id)
    }

    suspend fun create(till: Till): Till {

        val map = mapRepository.mapById(till.mapId)
            ?: throw IllegalArgumentException("Map not found")

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
            throw IllegalArgumentException("Invalid position")
        }

        return tillRepository.addTill(till)
    }

    suspend fun update(till: Till): Till {

        val map = mapRepository.mapById(till.mapId)
            ?: throw IllegalArgumentException("Map not found")

        val wallBlocks = wallBlockRepository.wallBlocksByMap(till.mapId)
        val departments = departmentRepository.departmentsByMap(till.mapId)
        val tills = tillRepository.tillsByMap(till.mapId).filter { it.id != till.id }
        val obstacles = wallBlocks.map { it.toRect()} + departments.map { it.toRect()} + tills.map { it.toRect()}

        val rect = till.toRect()

        val isValid = SpatialValidator.isValidPosition(
            rect,
            map,
            obstacles
        )

        if (!isValid) {
            throw IllegalArgumentException("Invalid position")
        }

        return tillRepository.updateTill(till)
    }
}