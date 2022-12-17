package it.bennes.jsonSchemaGenerator.encoding

interface Decoder {
    fun decode(input: String): String
}
