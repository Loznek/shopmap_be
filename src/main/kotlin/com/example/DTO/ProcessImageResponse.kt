import kotlinx.serialization.Serializable

@Serializable
data class Box(
    val startX: Double,
    val startY: Double,
    val width: Double,
    val height: Double,
    val name: String
)

@Serializable
data class ProcessImageResponse(
    val boxes: List<Box>
)