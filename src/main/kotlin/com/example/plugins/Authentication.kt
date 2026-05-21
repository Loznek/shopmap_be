import com.google.firebase.auth.FirebaseAuth
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.bearer

fun Application.configureAuthentication() {

    install(Authentication) {

        bearer("firebase") {

            authenticate { credential ->

                try {

                    val token =
                        FirebaseAuth.getInstance()
                            .verifyIdToken(credential.token)

                    FirebaseUserPrincipal(
                        uid = token.uid,
                        email = token.email,
                        displayName = token.name
                    )

                } catch (e: Exception) {
                    null
                }
            }
        }
    }
}