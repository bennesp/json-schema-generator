import encoding.Encoding
import exceptions.BadRequestException
import formats.Format
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.Request

fun Route.main() {
    get("/") {
        val params = call.request.queryParameters
        val log = call.application.environment.log

        val request = Request.fromParameters(params)
        val (url, data, inputFormat, outputFormat, encoding) = request

        Utils.logRequest(log, request, "Request received")

        // Get url content
        val content = if (url != null) {
            val response: HttpResponse = HttpClient.client.get(url)
            if (response.status.value !in 200..299) {
                throw BadRequestException("url returned invalid status code ${response.status.value} (not 2xx)")
            }
            response.bodyAsText()
        } else data ?: throw BadRequestException("No url or data provided")

        // Decode data
        val encoder = Encoding.fromString(encoding)
        val decodedContent = try {
            encoder.decoder.decode(content)
        } catch (e: Exception) {
            throw BadRequestException("error during decoding: ${e.message}")
        }

        // Generate schema
        val schemaService = SchemaService(Format.fromString(inputFormat), Format.fromString(outputFormat))
        val parsedContent = schemaService.parse(decodedContent)
        val schema = schemaService.generate(parsedContent)
        val schemaAsString = schemaService.toString(schema)

        // Respond to client
        call.respond(schemaAsString)
    }
}
