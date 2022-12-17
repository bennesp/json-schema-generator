import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.saasquatch.jsonschemainferrer.JsonSchemaInferrer
import format.Format

class SchemaGenerator(inputFormat: Format, outputFormat: Format) {
    private var inputMapper = ObjectMapper(inputFormat.factory)
    private var outputMapper = ObjectMapper(outputFormat.factory)
    private val inferrer = JsonSchemaInferrer.newBuilder().build()

    fun generate(content: String): String {
        val parsedTree = inputMapper.readTree(content)
        val result: JsonNode = inferrer.inferForSample(parsedTree)
        return outputMapper.writeValueAsString(result)
    }
}
