package it.bennes.jsonSchemaGenerator.encoding

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class EncodingTest {

    @Test
    fun `test fromString with valid input`() {
        assertEquals(Encoding.Nop, Encoding.fromString("nop"))
        assertEquals(Encoding.Base64, Encoding.fromString("base64"))
        assertEquals(Encoding.Hex, Encoding.fromString("hex"))
    }

    @Test
    fun `test fromString with invalid input`() {
        assertFailsWith<IllegalArgumentException> {
            Encoding.fromString("invalid")
        }
    }

    @Test
    fun `test decoder instances`() {
        assertEquals(NopDecoder::class, Encoding.Nop.decoder::class)
        assertEquals(Base64Decoder::class, Encoding.Base64.decoder::class)
        assertEquals(HexDecoder::class, Encoding.Hex.decoder::class)
    }
}
