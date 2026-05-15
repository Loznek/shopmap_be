package com.example.wallblocks

import com.example.geometry.SpatialValidator
import com.example.model.entity.WallBlock
import com.example.model.entity.toRect
import com.example.model.repository.DepartmentRepository
import com.example.model.repository.MapRepository
import com.example.model.repository.TillRepository
import com.example.model.repository.WallBlockRepository
import com.example.navigation.GridBuilder
import com.example.navigation.PathValidator

class WallBlockService(
    private val wallBlockRepository: WallBlockRepository,
    private val departmentRepository: DepartmentRepository,
    private val tillRepository: TillRepository,
    private val mapRepository: MapRepository,
    private val gridBuilder: GridBuilder,
    private val pathValidator: PathValidator
) {

    suspend fun getByMap(mapId: Int): List<WallBlock> {
        return wallBlockRepository.wallBlocksByMap(mapId)
    }

    suspend fun delete(id: Int) {
        wallBlockRepository.removeWallBlockById(id)
    }

    suspend fun create(wallBlock: WallBlock): WallBlock {

        val map = mapRepository.mapById(wallBlock.mapId)
            ?: throw IllegalArgumentException("Map not found")

        val wallBlocks = wallBlockRepository.wallBlocksByMap(wallBlock.mapId)
        val departments = departmentRepository.departmentsByMap(wallBlock.mapId)
        val tills = tillRepository.tillsByMap(wallBlock.mapId)
        val obstacles = wallBlocks.map { it.toRect()} + departments.map { it.toRect()} + tills.map { it.toRect()}

        val rect = wallBlock.toRect()

        // ✅ spatial validation
        val isValid = SpatialValidator.isValidPosition(
            rect,
            map,
            obstacles
        )

        if (!isValid) {
            throw IllegalArgumentException("Invalid position")
        }

        // ✅ path validation
        val walkablePoints = gridBuilder.buildWalkablePoints(
            map = map,
            wallBlocks = wallBlocks,
            departments = departments,
            tills = tills,
            newRect = rect,
            excludeWallId = -1,
            excludeDepartmentId = -1,
        )

        val till = tills.firstOrNull()
            ?: throw IllegalStateException("No till found")

        val reachable = pathValidator.pathExists(
            walkablePoints,
            map.entranceX.toInt() to map.entranceY.toInt(),
            tills[0].startX.toInt() to tills[0].startY.toInt()
        )

        if (!reachable) {
            throw IllegalArgumentException("No path to tills")
        }

        return wallBlockRepository.addWallBlock(wallBlock)
    }

    suspend fun update(wallBlock: WallBlock): WallBlock {

        val map = mapRepository.mapById(wallBlock.mapId)
            ?: throw IllegalArgumentException("Map not found")

        val wallBlocks = wallBlockRepository.wallBlocksByMap(wallBlock.mapId).filter { it.id != wallBlock.id }
        val departments = departmentRepository.departmentsByMap(wallBlock.mapId)
        val tills = tillRepository.tillsByMap(wallBlock.mapId)
        val obstacles = wallBlocks.map { it.toRect()} + departments.map { it.toRect()} + tills.map { it.toRect()}

        val rect = wallBlock.toRect()

        val isValid = SpatialValidator.isValidPosition(
            rect,
            map,
            obstacles
        )

        if (!isValid) {
            throw IllegalArgumentException("Invalid position")
        }

        val walkablePoints = gridBuilder.buildWalkablePoints(
            map = map,
            wallBlocks = wallBlocks,
            departments = departments,
            tills = tills,
            newRect = rect,
            excludeWallId = wallBlock.id!!,
            excludeDepartmentId = -1
        )

        val till = tills.firstOrNull()
            ?: throw IllegalStateException("No till found")

        val reachable = pathValidator.pathExists(
            walkablePoints,
            map.entranceX.toInt() to map.entranceY.toInt(),
            tills[0].startX.toInt() to tills[0].startY.toInt()
        )

        if (!reachable) {
            throw IllegalArgumentException("No path to tills")
        }

        return wallBlockRepository.updateWallBlock(wallBlock)
    }
}