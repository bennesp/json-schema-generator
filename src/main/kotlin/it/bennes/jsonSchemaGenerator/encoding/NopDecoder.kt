package it.bennes.jsonSchemaGenerator.encoding

class NopDecoder : Decoder {
    override fun decode(input: String): String = input
}
