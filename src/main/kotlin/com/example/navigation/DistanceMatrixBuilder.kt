package com.example.navigation

class DistanceMatrixBuilder(
    private val pathFinder: PathFinder
) {

    private val INF = 1_000_000_000

    fun build(
        walkablePoints: Set<Pair<Int, Int>>,
        points: List<Pair<Int, Int>>
    ): Array<IntArray> {

        val matrix =
            Array(points.size) {
                IntArray(points.size) {
                    INF
                }
            }

        for (i in points.indices) {
            println(
                "BFS $i/${points.size} start=${points[i]} " +
                        "walkable=${points[i] in walkablePoints}"
            )

            val start =
                System.currentTimeMillis()
            val distanceMap =
                pathFinder.bfsDistanceMap(
                    walkablePoints,
                    points[i]
                )

            println(
                "reachable=${distanceMap.size}"
            )
            println(
                "BFS finished in ${
                    System.currentTimeMillis() - start
                } ms"
            )
            for (j in points.indices) {

                if (i == j) {
                    matrix[i][j] = 0
                    continue
                }

                matrix[i][j] =
                    distanceMap[points[j]]
                        ?: INF
            }
        }

        return matrix
    }
}