package com.example.integration

import com.example.departments.DepartmentController
import com.example.departments.DepartmentService
import com.example.departments.TestData
import com.example.departments.departmentRoutes
import com.example.departments.dto.DepartmentResponse
import com.example.plugins.configureExceptionHandling
import com.example.plugins.configureSerialization
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

class DepartmentRouteIntegrationTest {

    private val service = mockk<DepartmentService>()

    private val controller =
        DepartmentController(service)

    @Test
    fun `GET department by id returns 200`() = testApplication {

        application {

            configureSerialization()
            configureExceptionHandling()

            routing {
                departmentRoutes(controller)
            }
        }

        val client = createClient {

            install(ContentNegotiation) {
                json()
            }
        }

        coEvery {
            service.get(1)
        } returns TestData.department()

        val response =
            client.get(
                "/departments/item/1"
            )

        assertEquals(
            HttpStatusCode.OK,
            response.status
        )

        val body =
            response.body<DepartmentResponse>()

        assertEquals(
            "Bakery",
            body.name
        )
    }
}