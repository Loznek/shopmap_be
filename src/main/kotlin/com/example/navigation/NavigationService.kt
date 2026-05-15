package com.example.navigation

import com.example.model.repository.DepartmentRepository
import com.example.model.repository.MapRepository
import com.example.model.repository.TillRepository
import com.example.model.repository.WallBlockRepository

class NavigationService(
    private val mapRepository: MapRepository,
    private val departmentRepository: DepartmentRepository,
    private val tillRepository: TillRepository,
    private val wallBlockRepository: WallBlockRepository
) {

    private val gridBuilder = GridBuilder()
    private val pathFinder = PathFinder()
    private val tspSolver = TspSolver(pathFinder)

    suspend fun calculateRoute(
        mapId: Int,
        destinationDepartmentIds: List<Int>
    ): List<Pair<Int, Int>> {

        val map = mapRepository.mapById(mapId)
            ?: throw IllegalArgumentException("Map not found")

        val departments = departmentRepository.departmentsByMap(mapId)
        val tills = tillRepository.tillsByMap(mapId)
        val wallBlocks = wallBlockRepository.wallBlocksByMap(mapId)

        if (tills.isEmpty()) {
            throw IllegalArgumentException("No tills found for map")
        }

        val relevantDepartments =
            departments.filter { it.id in destinationDepartmentIds }

        val destinationPoints = relevantDepartments.map {
            ((it.startX + it.width - 1).toInt()) to it.startY.toInt()
        }

        val walkablePoints = gridBuilder.buildWalkablePoints(
            map = map,
            wallBlocks = wallBlocks,
            departments = departments,
            tills = tills,
            newRect = null,
            excludeDepartmentId = -1,
            excludeWallId = -1
        )

        val start = map.entranceX.toInt() to map.entranceY.toInt()
        val end = tills.first().startX.toInt() to tills.first().startY.toInt()

        return tspSolver.solve(
            walkablePoints = walkablePoints,
            destinations = destinationPoints,
            start = start,
            end = end
        )
    }
}