package it.bennes.jsonSchemaGenerator

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import it.bennes.jsonSchemaGenerator.formats.Format
import it.bennes.jsonSchemaGenerator.model.JsonSchemaInferrer

class SchemaService(private val inferrer: JsonSchemaInferrer, inputFormat: Format, outputFormat: Format) {
    private var inputMapper = ObjectMapper(inputFormat.factory)
    private var outputMapper = ObjectMapper(outputFormat.factory)

    fun parse(input: String): JsonNode = inputMapper.readTree(input)

    fun generate(content: JsonNode): JsonNode = inferrer.inferForSample(content)

    fun toString(input: JsonNode): String = outputMapper.writeValueAsString(input)
}
