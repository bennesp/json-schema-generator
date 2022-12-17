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
        val (url, data, inputFormat, outputFormat, encoding, generate, selector) = request

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
        //   1. Parse data
        val schemaService = SchemaService(Format.fromString(inputFormat), Format.fromString(outputFormat))
        val parsedContent = schemaService.parse(decodedContent)

        //   2. Select data, if selector is provided
        val selectedContent = if (selector != null) {
            val node = parsedContent.at(selector)
            if (node.isMissingNode) {
                throw BadRequestException("selector did not match any node")
            }
            node
        } else {
            parsedContent
        }

        //   3. Generate schema, if generate is true
        val schema = if (generate) {
            schemaService.generate(selectedContent)
        } else {
            selectedContent
        }

        //   4. Encode schema
        val schemaAsString = schemaService.toString(schema)

        //   5. Respond with the schema to the client
        call.respond(schemaAsString)

    }
}
