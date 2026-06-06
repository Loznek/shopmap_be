package com.example.wallblocks

import com.example.exception.ComputationException
import com.example.exception.NotFoundException
import com.example.exception.ValidationException
import com.example.geometry.SpatialValidator
import com.example.model.entity.WallBlock
import com.example.model.entity.toRect
import com.example.model.repository.DepartmentRepository
import com.example.model.repository.MapRepository
import com.example.model.repository.TillRepository
import com.example.model.repository.WallBlockRepository
import com.example.navigation.GridBuilder
import com.example.navigation.PathFinder
import com.example.navigation.PathValidator
import com.example.navigation.toGrid
import com.example.tills.dto.toResponse
import com.example.wallblocks.dto.toResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import kotlin.text.toIntOrNull

class WallBlockService(
    private val wallBlockRepository: WallBlockRepository,
    private val departmentRepository: DepartmentRepository,
    private val tillRepository: TillRepository,
    private val mapRepository: MapRepository,
    private val gridBuilder: GridBuilder,
    private val pathFinder: PathFinder
) {

    suspend fun get(wallBlockId:Int):WallBlock {

        return wallBlockRepository.wallBlockById(wallBlockId)?: throw NotFoundException(
            "WallBlock $wallBlockId not found"
        )

    }

    suspend fun getByMap(mapId: Int): List<WallBlock> {
        mapRepository.mapById(mapId)
            ?: throw NotFoundException(
                "Map $mapId not found"
            )
        return wallBlockRepository.wallBlocksByMap(mapId)
    }

    suspend fun delete(id: Int) {
        val deleted = wallBlockRepository.removeWallBlockById(id)
        if (!deleted) {
            throw NotFoundException(
                "WallBlock $id not found"
            )
        }
    }

    suspend fun create(wallBlock: WallBlock): WallBlock {

        val map = mapRepository.mapById(wallBlock.mapId)
            ?: throw NotFoundException("Map not found")

        val wallBlocks = wallBlockRepository
            .wallBlocksByMap(wallBlock.mapId)


        val departments = departmentRepository
            .departmentsByMap(wallBlock.mapId)

        val tills = tillRepository
            .tillsByMap(wallBlock.mapId)

        val obstacles = wallBlocks.map { it.toRect()} + departments.map { it.toRect()} + tills.map { it.toRect()}

        val rect = wallBlock.toRect()

        // ✅ spatial validation
        val isValid = SpatialValidator.isValidPosition(
            rect,
            map,
            obstacles
        )
        if (!isValid) {
            throw ValidationException("Invalid WallBlock position")
        }

        // ✅ path validation
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
            throw ComputationException("Wall blocks access to tills")
        }

        return wallBlockRepository.addWallBlock(wallBlock)
    }

    suspend fun update(wallBlock: WallBlock): WallBlock {

        val map = mapRepository.mapById(wallBlock.mapId)
            ?: throw NotFoundException("Map not found")

        val wallBlocks = wallBlockRepository
            .wallBlocksByMap(wallBlock.mapId)

        val departments = departmentRepository
            .departmentsByMap(wallBlock.mapId)

        val tills = tillRepository
            .tillsByMap(wallBlock.mapId)

        val obstacles = wallBlocks.map { it.toRect()} + departments.map { it.toRect()} + tills.map { it.toRect()}

        val rect = wallBlock.toRect()

        val isValid = SpatialValidator.isValidPosition(
            rect,
            map,
            obstacles
        )

        if (!isValid) {
            throw ValidationException("Invalid position for modified WallBlock")
        }

        val walkablePoints = gridBuilder.buildWalkablePoints(
            map = map,
            wallBlocks = wallBlocks,
            departments = departments,
            tills = tills,
            newRect = rect,
            excludeDepartmentId = -1,
            excludeWallId=wallBlock.id!!
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
            throw ComputationException("WallBlock blocks access to tills")
        }

        return wallBlockRepository.updateWallBlock(wallBlock)
    }
}