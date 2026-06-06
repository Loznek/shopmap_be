package com.example.navigation

import com.example.departments.dto.DepartmentResponse
import com.example.departments.dto.toResponse
import com.example.exception.ComputationException
import com.example.model.entity.Department
import com.example.model.entity.ProductPosition
import com.example.model.repository.DepartmentRepository
import com.example.model.repository.MapRepository
import com.example.model.repository.ProductRepository
import com.example.model.repository.TillRepository
import com.example.model.repository.WallBlockRepository
import com.example.navigation.dto.RoutePlanResponse
import com.example.navigation.dto.RoutePlanningProduct
import com.example.navigation.optimizer.HeldKarpSolver
import com.example.navigation.optimizer.NearestNeighborSolver
import com.example.navigation.optimizer.RouteOptimizer
import com.example.navigation.optimizer.TwoOptSolver
import io.ktor.server.plugins.NotFoundException
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.roundToInt

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

    private val ACCESS_OFFSET = 1

    suspend fun calculateRoute(
        mapId: Int,
        products: List<RoutePlanningProduct>
    ): List<Pair<Double, Double>> {

        val map = mapRepository.mapById(mapId)
            ?: throw NotFoundException("Map not found")

        val departments = departmentRepository.departmentsByMap(mapId)
        val tills = tillRepository.tillsByMap(mapId)
        if (tills.isEmpty()) {
            throw NotFoundException(
                "No tills found for map"
            )
        }

        val wallBlocks = wallBlockRepository.wallBlocksByMap(mapId)

        if (tills.isEmpty()) {
            throw IllegalArgumentException("No tills found for map")
        }

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



        var walkablePoints = gridBuilder.buildWalkablePoints(
            map = map,
            wallBlocks = wallBlocks,
            departments = departments,
            tills = tills,
            newRect = null,
            excludeDepartmentId = -1,
            excludeWallId = -1
        )

        destinationPoints.forEach { point ->

            if (point !in walkablePoints) {

                throw ComputationException(
                    "Destination point $point is not reachable"
                )
            }
        }

        val startPoint = map.entranceX.toGrid() to map.entranceY.toGrid()

        val till = tills[0]
        // Ensure till is reachable
        val endPoint= (till.startX + till.width / 2).toGrid() to (till.startY + till.height / 2).toGrid()
        val points = listOf(startPoint) + destinationPoints + listOf(endPoint)

        println("Walkable points: ${walkablePoints.size}")
        println("Important points: ${points.size}")
        val startTime = System.currentTimeMillis()
        val distances = distanceMatrixBuilder.build(walkablePoints, points)
        println(
            "Distance matrix: ${
                System.currentTimeMillis() - startTime
            } ms"
        )


        for (i in distances.indices) {
            for (j in distances.indices) {

                if (
                    i != j &&
                    distances[i][j] > 1_000_000_000
                ) {

                    throw ComputationException(
                        "Some destinations are unreachable"
                    )
                }
            }
        }


        val optimizer =
            optimizerFactory.getOptimizer(
                destinationPoints.size
            )

        val optimizeStartTime = System.currentTimeMillis()
        val order =
            optimizer.solveOrder(
                distances,
                destinationPoints.size,
                points.lastIndex
            )
        println(
            "Held_karp: ${
                System.currentTimeMillis() - optimizeStartTime
            } ms"
        )


        if (order.isEmpty()) {

            throw ComputationException(
                "Failed to compute route"
            )
        }

        val orderedStops =
            order.map {
                points[it]
            }

        val finalPath= pathFinder.computeFullPath(
            walkablePoints,
            orderedStops
        )

        if (finalPath.isEmpty()) {
            throw ComputationException(
                "Failed to reconstruct route"
            )
        }

        val path = finalPath.map {
            it.first / 2.0 to it.second / 2.0
        }
        /*generateDebugFloorPlan(
            departments = departments.map { it.toResponse() },
            route = path,
            destinationPoints = destinationPoints.map {
                it.first / 2.0 to it.second / 2.0
            },
            mapWidth = map.width,
            mapHeight = map.height
        )*/

        return path
    }



    private fun getProductLocation(
        department: Department,
        position: ProductPosition
    ): Pair<Int, Int> {

        val centerX =
            (department.startX + department.width / 2).toGrid()

        val centerY =
            (department.startY + department.height / 2).toGrid()

        return when (position) {

            ProductPosition.TOP ->
                centerX to
                        (department.startY.toGrid() - ACCESS_OFFSET)

            ProductPosition.BOTTOM ->
                centerX to
                        ((department.startY + department.height).toGrid() + ACCESS_OFFSET)

            ProductPosition.LEFT ->
                (department.startX.toGrid() - ACCESS_OFFSET) to
                        centerY

            ProductPosition.RIGHT ->
                ((department.startX + department.width).toGrid() + ACCESS_OFFSET) to
                        centerY
        }
    }

    fun Double.toGrid(): Int =
        (this * 2).roundToInt()



    fun generateDebugFloorPlan(
        departments: List<DepartmentResponse>,
        route: List<Pair<Double, Double>>,
        destinationPoints: List<Pair<Double, Double>>,
        mapWidth: Double,
        mapHeight: Double
    ) {

        val scale = 40

        val widthPx = (mapWidth * scale).toInt()
        val heightPx = (mapHeight * scale).toInt()

        val image = BufferedImage(
            widthPx,
            heightPx,
            BufferedImage.TYPE_INT_RGB
        )

        val g = image.createGraphics()

        g.color = Color.WHITE
        g.fillRect(0, 0, widthPx, heightPx)

        g.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        )

        g.font = Font(
            "Arial",
            Font.BOLD,
            60
        )

        g.stroke = BasicStroke(10f)

        // ---------------------------
        // Departments
        // ---------------------------
        departments.forEach { department ->

            val x = (department.startX * scale).toInt()
            val y = (department.startY * scale).toInt()

            val w = (department.width * scale).toInt()
            val h = (department.height * scale).toInt()

            g.color = Color.BLACK

            g.drawRect(
                x,
                y,
                w,
                h
            )

            g.color = Color.BLUE

            g.drawString(
                department.name,
                x + 5,
                y + 55
            )
        }

        // ---------------------------
        // Route
        // ---------------------------
        if (route.size > 1) {

            g.color = Color.RED
            g.stroke = BasicStroke(20f)

            for (i in 0 until route.size - 1) {

                val p1 = route[i]
                val p2 = route[i + 1]

                g.drawLine(
                    (p1.first * scale).toInt(),
                    (p1.second * scale).toInt(),
                    (p2.first * scale).toInt(),
                    (p2.second * scale).toInt()
                )
            }
        }

        g.color = Color.ORANGE

        destinationPoints.forEach {

            g.fillOval(
                (it.first * scale).toInt() - 16,
                (it.second * scale).toInt() - 16,
                32,
                32
            )
        }

        // ---------------------------
        // Start point
        // ---------------------------
        if (route.isNotEmpty()) {

            val start = route.first()

            g.color = Color.GREEN

            g.fillOval(
                (start.first * scale).toInt() - 12,
                (start.second * scale).toInt() - 12,
                24,
                24
            )

            g.drawString(
                "START",
                (start.first * scale).toInt() + 15,
                (start.second * scale).toInt()
            )
        }

        // ---------------------------
        // End point (Till)
        // ---------------------------
        if (route.isNotEmpty()) {

            val end = route.last()

            g.color = Color.BLUE

            g.fillOval(
                (end.first * scale).toInt() - 12,
                (end.second * scale).toInt() - 12,
                24,
                24
            )

            g.drawString(
                "END",
                (end.first * scale).toInt() + 15,
                (end.second * scale).toInt()
            )
        }

        g.dispose()

        ImageIO.write(
            image,
            "png",
            File("debug_route_floorplan.png")
        )
    }
}