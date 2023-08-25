package it.bennes.jsonSchemaGenerator.encoding

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class Base64DecoderTest {

    @Test
    fun `test decode with valid input`() {
        val decoder = Base64Decoder()
        assertEquals("This is a test", decoder.decode("VGhpcyBpcyBhIHRlc3Q="))
    }

    @Test
    fun `test decode with invalid input`() {
        val decoder = Base64Decoder()
        assertFailsWith<IllegalArgumentException> {
            decoder.decode("This is a test")
        }
    }
}
