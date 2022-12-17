package formats

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

enum class Format(val factory: JsonFactory) {
    Json(JsonFactory()),
    Yaml(YAMLFactory());

    companion object {
        fun fromString(f: String): Format {
            return when (f.lowercase()) {
                "json" -> Json
                "yaml" -> Yaml
                else -> throw IllegalArgumentException("invalid format")
            }
        }
    }
}
