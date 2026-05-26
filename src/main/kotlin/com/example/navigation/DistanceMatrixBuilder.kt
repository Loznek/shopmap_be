package com.example.navigation

class DistanceMatrixBuilder(
    private val pathFinder: PathFinder
) {

    fun build(
        walkablePoints: Set<Pair<Int, Int>>,
        points: List<Pair<Int, Int>>
    ): Array<IntArray> {

        val matrix =
            Array(points.size) {
                IntArray(points.size) {
                    Int.MAX_VALUE
                }
            }

        for (i in points.indices) {

            val distanceMap =
                pathFinder.bfsDistanceMap(
                    walkablePoints,
                    points[i]
                )

            for (j in points.indices) {

                if (i == j) {
                    matrix[i][j] = 0
                    continue
                }

                matrix[i][j] =
                    distanceMap[points[j]]
                        ?: Int.MAX_VALUE
            }
        }

        return matrix
    }
}