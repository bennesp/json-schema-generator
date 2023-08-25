package it.bennes.jsonSchemaGenerator.formats

import java.lang.IllegalArgumentException
import kotlin.test.Test
import kotlin.test.assertFailsWith

class FormatTest {

    @Test
    fun `test fromString`() {
        val format = Format.fromString("json")
        assert(format == Format.Json)

        val format2 = Format.fromString("yaml")
        assert(format2 == Format.Yaml)
    }

    @Test
    fun `test fromString with invalid format`() {
        assertFailsWith<IllegalArgumentException> {
            Format.fromString("invalid")
        }
    }
}
