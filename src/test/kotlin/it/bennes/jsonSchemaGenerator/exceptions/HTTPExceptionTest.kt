package it.bennes.jsonSchemaGenerator.exceptions

import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals

class HTTPExceptionTest {
    @Test
    fun `test constructor`() {
        val exception = HTTPException(HttpStatusCode.NotFound, "not found")
        assertEquals("not found", exception.message)
        assertEquals(HttpStatusCode.NotFound, exception.statusCode)
    }
}
