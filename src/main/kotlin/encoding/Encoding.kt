package encoding

enum class Encoding(val decoder: Decoder) {
    Nop(NopDecoder()),
    Base64(Base64Decoder()),
    Hex(HexDecoder());

    companion object {
        fun fromString(d: String): Encoding {
            return when (d.lowercase()) {
                "nop" -> Nop
                "base64" -> Base64
                "hex" -> Hex
                else -> throw IllegalArgumentException("Invalid encoding")
            }
        }
    }
}
