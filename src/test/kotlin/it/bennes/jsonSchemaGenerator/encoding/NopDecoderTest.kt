package it.bennes.jsonSchemaGenerator.encoding

import kotlin.test.Test
import kotlin.test.assertEquals

class NopDecoderTest {

    @Test
    fun decode() {
        val decoder = NopDecoder()
        assertEquals("This is a test", decoder.decode("This is a test"))
    }
}
