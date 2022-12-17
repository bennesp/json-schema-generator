package encoding

class Base64Decoder : Decoder {
    override fun decode(input: String): String = java.util.Base64.getDecoder().decode(input).decodeToString()
}
