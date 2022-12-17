package encoding

enum class Encoding(val decoder: Decoder) {
    Text(TextDecoder()),
    Base64(Base64Decoder()),
    Hex(HexDecoder());

    companion object {
        fun fromString(d: String): Encoding {
            return when (d.lowercase()) {
                "text" -> Text
                "base64" -> Base64
                "hex" -> Hex
                else -> throw IllegalArgumentException("Invalid encoding")
            }
        }
    }
}
