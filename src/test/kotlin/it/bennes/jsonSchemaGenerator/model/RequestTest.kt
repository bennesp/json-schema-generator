package it.bennes.jsonSchemaGenerator.model

import io.ktor.http.*
import it.bennes.jsonSchemaGenerator.exceptions.BadRequestException
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RequestTest {

    @Test
    fun `no url or data`() {
        val params = Parameters.build {
            append("input", "json")
            append("output", "json")
            append("encoding", "base64")
            append("generate", "true")
        }
        val exception = assertFailsWith<BadRequestException> {
            Request.fromParameters(params)
        }
        assertEquals("either url or data must be provided", exception.message)
    }

    @Test
    fun `invalid URL format`() {
        val params = Parameters.build {
            append("url", "example.com")
            append("input", "json")
            append("output", "json")
            append("encoding", "base64")
            append("generate", "true")
        }
        val exception = assertFailsWith<BadRequestException> {
            Request.fromParameters(params)
        }
        assertEquals("invalid url", exception.message)
    }

    @TestFactory
    fun `URL must not be local`() = listOf(
        "http://10.0.1.2",
        "http://127.0.0.1",
        "http://0.0.0.0",
        "http://192.168.2.3"
    ).map { url ->
        val exception = assertFailsWith<BadRequestException> {
            Request.fromParameters(Parameters.build {
                append("url", url)
            })
        }
        DynamicTest.dynamicTest("URL $url must not be local") {
            assertEquals("url must not be local", exception.message)
            assertEquals(HttpStatusCode.BadRequest, exception.statusCode)
        }
    }

    @Test
    fun `test default values`() {
        val expected = Request(
            url = null,
            data = "data",
            inputFormat = "yaml",
            outputFormat = "json",
            encoding = "nop",
            generate = true,
            selector = null,
        )

        val actual = Request.fromParameters(
            Parameters.build {
                append("data", "data")
            }
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `test with valid data`() {
        val expected = Request(
            url = null,
            data = "data",
            inputFormat = "json",
            outputFormat = "json",
            encoding = "nop",
            generate = true,
            selector = null,
        )

        val actual = Request.fromParameters(
            Parameters.build {
                append("data", "data")
                append("input", "json")
                append("output", "json")
                append("encoding", "nop")
                append("generate", "true")
            }
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `test with invalid generate`() {
        val exception = assertFailsWith<BadRequestException> {
            Request.fromParameters(
                Parameters.build {
                    append("data", "data")
                    append("generate", "invalid")
                }
            )
        }
        assertEquals("generate must be 'true' or 'false'", exception.message)
    }

    @Test
    fun `test with blank data`() {
        val exception = assertFailsWith<BadRequestException> {
            Request.fromParameters(
                Parameters.build {
                    append("data", "")
                }
            )
        }
        assertEquals("data must not be blank", exception.message)
    }

    @Test
    fun `test fromParameters with URL`() {
        val expected = Request(
            url = "https://example.com",
            data = null,
            inputFormat = "json",
            outputFormat = "json",
            encoding = "base64",
            generate = true,
            selector = "/a/b/c",
        )

        val actual = Request.fromParameters(
            Parameters.build {
                append("url", "https://example.com")
                append("input", "json")
                append("output", "json")
                append("encoding", "base64")
                append("generate", "true")
                append("selector", "/a/b/c")
            }
        )

        assertEquals(expected, actual)
    }

}
