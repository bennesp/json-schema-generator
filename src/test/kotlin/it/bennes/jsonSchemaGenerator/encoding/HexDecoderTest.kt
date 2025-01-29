package it.bennes.jsonSchemaGenerator.encoding


import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class HexDecoderTest {

    @Test
    fun `test decode with valid input`() {
        val decoder = HexDecoder()
        assertEquals("This is a test", decoder.decode("5468697320697320612074657374"))
    }

    @Test
    fun `test decode with invalid input`() {
        val decoder = HexDecoder()
        assertFailsWith<IllegalArgumentException> {
            decoder.decode("This is a test")
        }
    }
}
