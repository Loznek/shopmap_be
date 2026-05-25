package com.example.shoppingList


import FirebaseUserPrincipal
import com.example.controller.ShoppingListController
import com.example.shoppingList.dto.CreateShoppingListRequest
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.shoppingListRoutes(
    controller: ShoppingListController
) {

    authenticate("firebase") {

        route("/shopping-lists") {

            get {

                val principal =
                    call.authentication
                        .principal<FirebaseUserPrincipal>()
                        ?: return@get call.respond(
                            HttpStatusCode.Unauthorized
                        )

                call.respond(
                    controller.getLists(principal)
                )
            }

            post {

                val principal =
                    call.authentication
                        .principal<FirebaseUserPrincipal>()
                        ?: return@post call.respond(
                            HttpStatusCode.Unauthorized
                        )

                val request =
                    call.receive<CreateShoppingListRequest>()

                call.respond(
                    HttpStatusCode.Created,
                    controller.createList(
                        principal,
                        request
                    )
                )
            }

            get("/{id}") {

                val principal =
                    call.authentication
                        .principal<FirebaseUserPrincipal>()
                        ?: return@get call.respond(
                            HttpStatusCode.Unauthorized
                        )

                val id =
                    call.parameters["id"]?.toIntOrNull()
                        ?: return@get call.respond(
                            HttpStatusCode.BadRequest
                        )

                call.respond(
                    controller.getList(
                        principal,
                        id
                    )
                )
            }

            delete("/{id}") {

                val principal =
                    call.authentication
                        .principal<FirebaseUserPrincipal>()
                        ?: return@delete call.respond(
                            HttpStatusCode.Unauthorized
                        )

                val id =
                    call.parameters["id"]?.toIntOrNull()
                        ?: return@delete call.respond(
                            HttpStatusCode.BadRequest
                        )

                controller.deleteList(
                    principal,
                    id
                )

                call.respond(
                    HttpStatusCode.NoContent
                )
            }
        }
    }
}