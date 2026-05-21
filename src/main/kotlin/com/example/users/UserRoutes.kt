import com.example.users.UserController
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.response.respond

fun Route.userRoutes(
    userController: UserController
) {

    authenticate("firebase") {

        route("/me") {

            get {

                val principal =
                    call.authentication.principal<FirebaseUserPrincipal>()
                        ?: return@get call.respond(
                            HttpStatusCode.Unauthorized
                        )

                val response = userController.me(principal)

                call.respond(response)
            }
        }
    }
}