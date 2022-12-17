package encoding

class HexDecoder : Decoder {
    override fun decode(input: String): String = input.chunked(2).map { it.toInt(16).toChar() }.joinToString("")
}
