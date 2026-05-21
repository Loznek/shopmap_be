interface UserRepository {

    suspend fun getByFirebaseUid(firebaseUid: String): AppUser?

    suspend fun create(
        firebaseUid: String,
        email: String?,
        displayName: String?
    ): AppUser
}