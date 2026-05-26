package com.example.navigation

import com.example.model.entity.Department
import com.example.model.entity.ProductPosition
import com.example.model.repository.DepartmentRepository
import com.example.model.repository.MapRepository
import com.example.model.repository.ProductRepository
import com.example.model.repository.TillRepository
import com.example.model.repository.WallBlockRepository
import com.example.navigation.dto.RoutePlanningProduct
import com.example.navigation.optimizer.HeldKarpSolver
import com.example.navigation.optimizer.NearestNeighborSolver
import com.example.navigation.optimizer.RouteOptimizer
import com.example.navigation.optimizer.TwoOptSolver

class NavigationService(
    private val mapRepository: MapRepository,
    private val departmentRepository: DepartmentRepository,
    private val tillRepository: TillRepository,
    private val wallBlockRepository: WallBlockRepository,
    private val gridBuilder: GridBuilder,
    private val pathFinder: PathFinder,
    private val distanceMatrixBuilder: DistanceMatrixBuilder,

    private val optimizerFactory: RouteOptimizerFactory

) {
    /*
    private val gridBuilder = GridBuilder()
    private val pathFinder = PathFinder()
    private val distanceMatrixBuilder = DistanceMatrixBuilder(pathFinder)
    private val heldKarpSolver = HeldKarpSolver()
    private val twoOptSolver =
        TwoOptSolver()

    private val nearestNeighborSolver =
        NearestNeighborSolver(
            twoOptSolver
        )
    */
    suspend fun calculateRoute(
        mapId: Int,
        products: List<RoutePlanningProduct>
    ): List<Pair<Int, Int>> {

        val map = mapRepository.mapById(mapId)
            ?: throw IllegalArgumentException("Map not found")

        val departments = departmentRepository.departmentsByMap(mapId)
        val tills = tillRepository.tillsByMap(mapId)
        val wallBlocks = wallBlockRepository.wallBlocksByMap(mapId)

        if (tills.isEmpty()) {
            throw IllegalArgumentException("No tills found for map")
        }
/*

        val relevantDepartments =
            departments.filter { it.id in destinationDepartmentIds }

        val destinationPoints = relevantDepartments.map {
            ((it.startX + it.width - 1).toInt()) to it.startY.toInt()
        }
*/

        val departmentMap =
            departments
                .associateBy { it.id }


        val destinationPoints =
            products.mapNotNull { product ->

                val department =
                    departmentMap[product.departmentId]
                        ?: return@mapNotNull null

                getProductLocation(
                    department,
                    product.position
                )
            }.distinct()

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


        val points = listOf(start) + destinationPoints + listOf(end)


        val distances = distanceMatrixBuilder.build(walkablePoints, points)

        val optimizer =
            optimizerFactory.getOptimizer(
                destinationPoints.size
            )

        val order =
            optimizer.solveOrder(
                distances,
                destinationPoints.size,
                points.lastIndex
            )

        val orderedStops =
            order.map {
                points[it]
            }

        return pathFinder.computeFullPath(
            walkablePoints,
            orderedStops
        )
        /*
        return heldKarpSolver.solve(
            walkablePoints = walkablePoints,
            destinations = destinationPoints,
            start = start,
            end = end
        )*/
    }



    private fun getProductLocation(
        department: Department,
        position: ProductPosition
    ): Pair<Int, Int> {

        return when(position) {

            ProductPosition.TOP ->
                (
                        department.startX + department.width / 2
                        ).toInt() to department.startY.toInt()

            ProductPosition.BOTTOM ->
                (
                        department.startX + department.width / 2
                        ).toInt() to (
                        department.startY + department.height
                        ).toInt()

            ProductPosition.LEFT ->
                department.startX.toInt() to (
                        department.startY + department.height / 2
                        ).toInt()

            ProductPosition.RIGHT ->
                (
                        department.startX + department.width
                        ).toInt() to (
                        department.startY + department.height / 2
                        ).toInt()
        }
    }
}