import kotlinx.serialization.Serializable

@Serializable
data class AppUserResponse(
    val id: String,
    val firebaseUid: String,
    val email: String?,
    val displayName: String?
)

