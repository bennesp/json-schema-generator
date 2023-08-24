package it.bennes.jsonSchemaGenerator

import com.fasterxml.jackson.core.JsonParseException
import it.bennes.jsonSchemaGenerator.formats.Format
import it.bennes.jsonSchemaGenerator.model.JsonSchemaInferrer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class SchemaServiceTest {

    @Test
    fun `valid parse`() {
        val schemaService = SchemaService(JsonSchemaInferrer(), Format.Json, Format.Json)
        val expected = """{"a":1}"""
        val actual = schemaService.parse(expected)

        assertEquals(expected, actual.toString())
    }

    @Test
    fun `invalid parse`() {
        val schemaService = SchemaService(JsonSchemaInferrer(), Format.Json, Format.Json)
        val expected = """{"a":1"""
        assertFailsWith<JsonParseException> {
            schemaService.parse(expected)
        }
    }

    @Test
    fun `valid simple generate`() {
        val schemaService = SchemaService(JsonSchemaInferrer(), Format.Json, Format.Json)
        val input = """{"a":1}"""
        val expected = """{"${"$"}schema":"http://json-schema.org/draft-04/schema#","type":"object","properties":{"a":{"type":"integer"}}}"""
        val actual = schemaService.generate(schemaService.parse(input))

        assertEquals(expected, actual.toString())
    }

    @Test
    fun `invalid generate`() {
        val schemaService = SchemaService(JsonSchemaInferrer(), Format.Json, Format.Json)
        val input = """{"a":1"""
        assertFailsWith<JsonParseException> {
            schemaService.generate(schemaService.parse(input))
        }
    }

    @Test
    fun `test parse and toString to be reversible`() {
        val schemaService = SchemaService(JsonSchemaInferrer(), Format.Json, Format.Json)
        val input = """{"a":1}"""
        val expected = """{"a":1}"""
        val actual = schemaService.toString(schemaService.parse(input))

        assertEquals(expected, actual)
    }
}
