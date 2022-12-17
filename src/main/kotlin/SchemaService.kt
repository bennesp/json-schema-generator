import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.saasquatch.jsonschemainferrer.JsonSchemaInferrer
import formats.Format

class SchemaService(inputFormat: Format, outputFormat: Format) {
    private var inputMapper = ObjectMapper(inputFormat.factory)
    private var outputMapper = ObjectMapper(outputFormat.factory)
    private val inferrer = JsonSchemaInferrer.newBuilder().build()

    fun parse(input: String): JsonNode = inputMapper.readTree(input)

    fun generate(content: JsonNode): JsonNode = inferrer.inferForSample(content)

    fun toString(input: JsonNode): String = outputMapper.writeValueAsString(input)
}
