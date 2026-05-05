import kotlinx.serialization.Serializable

@Serializable
data class PythonRequest(
    val image_path: String,
    val map_width: Int,
    val map_height: Int
)