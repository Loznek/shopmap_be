import com.example.wallblocks.WallBlockService
import com.example.wallblocks.dto.CreateWallBlockRequest
import com.example.wallblocks.dto.UpdateWallBlockRequest
import com.example.wallblocks.dto.toEntity
import com.example.wallblocks.dto.toResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class WallBlockController(
    private val service: WallBlockService
) {

    suspend fun getByMap(call: ApplicationCall) {
        val mapId = call.parameters["mapId"]?.toIntOrNull()
            ?: return call.respond(HttpStatusCode.BadRequest, "Invalid mapId")

        val result = service.getByMap(mapId)
        call.respond(result.map { it.toResponse() })
    }

    suspend fun create(call: ApplicationCall) {
        try {
            val request = call.receive<CreateWallBlockRequest>()

            val result = service.create(request.toEntity())

            call.respond(HttpStatusCode.Created, result.toResponse())

        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid request")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Unexpected error")
        }
    }

    suspend fun update(call: ApplicationCall) {
        try {
            val request = call.receive<UpdateWallBlockRequest>()

            val result = service.update(request.toEntity())

            call.respond(HttpStatusCode.OK, result.toResponse())

        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid request")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Unexpected error")
        }
    }

    suspend fun delete(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(HttpStatusCode.BadRequest, "Invalid id")

        service.delete(id)

        call.respond(HttpStatusCode.NoContent)
    }
}