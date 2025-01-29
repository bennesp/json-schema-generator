package it.bennes.jsonSchemaGenerator.exceptions

import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals

class BadRequestExceptionTest {

    @Test
    fun `test constructor`() {
        val exception = BadRequestException("message")
        assertEquals("message", exception.message)
        assertEquals(HttpStatusCode.BadRequest, exception.statusCode)
    }
}
