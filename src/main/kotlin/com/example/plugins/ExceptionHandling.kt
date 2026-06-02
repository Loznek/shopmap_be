package com.example.plugins

import com.example.exception.AuthenticationException
import com.example.exception.AuthorizationException
import com.example.exception.ComputationException
import com.example.exception.ErrorResponse
import com.example.exception.ExternalServiceException
import com.example.exception.ValidationException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

fun Application.configureExceptionHandling() {

    install(StatusPages) {

        exception<ValidationException> { call, cause ->

            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    status = 400,
                    message = cause.message
                        ?: "Validation failed"
                )
            )
        }

        exception<AuthorizationException> { call, cause ->

            call.respond(
                HttpStatusCode.Forbidden,
                ErrorResponse(
                    status = 403,
                    message = cause.message
                        ?: "Forbidden"
                )
            )
        }

        exception<AuthenticationException> { call, cause ->

            call.respond(
                HttpStatusCode.Unauthorized,
                ErrorResponse(
                    status = 401,
                    message = cause.message
                        ?: "Authentication failed"
                )
            )
        }

        exception<NotFoundException> { call, cause ->

            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(
                    status = 404,
                    message = cause.message
                        ?: "Resource not found"
                )
            )
        }

        exception<ExternalServiceException> { call, cause ->

            call.respond(
                HttpStatusCode.BadGateway,
                ErrorResponse(
                    status = 502,
                    message = cause.message
                        ?: "External service failed"
                )
            )
        }

        exception<ComputationException> { call, cause ->

            call.respond(
                HttpStatusCode.UnprocessableEntity,
                ErrorResponse(
                    status = 422,
                    message = cause.message
                        ?: "Computation failed"
                )
            )
        }

        exception<Throwable> { call, cause ->

            println("Unhandled exception: ${cause.message}")
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    message = "Internal server error",
                    status = 500
                )
            )
        }
    }
}