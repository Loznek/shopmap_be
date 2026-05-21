import com.example.db.mapping.suspendTransaction
import com.example.model.mapping.AppUserDAO
import com.example.model.mapping.AppUserTable
import com.example.model.mapping.daoToModel

class PostgresUserRepository : UserRepository {

    override suspend fun getByFirebaseUid(
        firebaseUid: String
    ): AppUser? = suspendTransaction {

        AppUserDAO.find {
            AppUserTable.firebaseUid eq firebaseUid
        }
            .firstOrNull()
            ?.let(::daoToModel)
    }

    override suspend fun create(
        firebaseUid: String,
        email: String?,
        displayName: String?
    ): AppUser = suspendTransaction {

        val dao = AppUserDAO.new {
            this.firebaseUid = firebaseUid
            this.email = email
            this.displayName = displayName
        }

        daoToModel(dao)
    }
}