package it.bennes.jsonSchemaGenerator

import it.bennes.jsonSchemaGenerator.encoding.Encoding
import it.bennes.jsonSchemaGenerator.exceptions.BadRequestException
import it.bennes.jsonSchemaGenerator.formats.Format
import io.ktor.client.statement.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import it.bennes.jsonSchemaGenerator.model.JsonSchemaInferrer
import it.bennes.jsonSchemaGenerator.model.Request

fun Route.main() {
    get("/") {
        val params = call.request.queryParameters
        val log = call.application.environment.log

        val request = Request.fromParameters(params)
        val (url, data, inputFormat, outputFormat, encoding, generate, selector) = request

        Utils.logRequest(log, request, "Request received")

        // Get url content
        val content = if (url != null) {
            HttpClient.get(url).bodyAsText()
        } else data ?: throw BadRequestException("No url or data provided")

        // Generate schema
        val schemaAsString = generateSchemaAsString(encoding, content, inputFormat, outputFormat, selector, generate)

        // Respond with the schema to the client
        call.respond(schemaAsString)

    }
}

private fun generateSchemaAsString(
    encoding: String,
    content: String,
    inputFormat: String,
    outputFormat: String,
    selector: String?,
    generate: Boolean,
): String {
    // Generate schema
    //   1. Decode data
    val encoder = Encoding.fromString(encoding)
    val decodedContent = try {
        encoder.decoder.decode(content)
    } catch (e: Exception) {
        throw BadRequestException("error during decoding: ${e.message}")
    }

    //   2. Parse data
    val inferrer = JsonSchemaInferrer()
    val schemaService = SchemaService(inferrer, Format.fromString(inputFormat), Format.fromString(outputFormat))
    val parsedContent = schemaService.parse(decodedContent)

    //   3. Select data, if selector is provided
    val selectedContent = if (selector != null) {
        val node = parsedContent.at(selector)
        if (node.isMissingNode) {
            throw BadRequestException("selector did not match any node")
        }
        node
    } else {
        parsedContent
    }

    //   4. Generate schema, if generate is true
    val schema = if (generate) {
        schemaService.generate(selectedContent)
    } else {
        selectedContent
    }

    //   5. Encode schema
    return schemaService.toString(schema)
}
