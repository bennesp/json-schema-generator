package it.bennes.jsonSchemaGenerator.model

import com.fasterxml.jackson.databind.JsonNode
import com.saasquatch.jsonschemainferrer.JsonSchemaInferrer

interface IJsonSchemaInferrer {
    fun inferForSample(content: JsonNode): JsonNode
}

class JsonSchemaInferrer : IJsonSchemaInferrer {
    private val inferrer: JsonSchemaInferrer = JsonSchemaInferrer.newBuilder().build()
    override fun inferForSample(content: JsonNode): JsonNode {
        return inferrer.inferForSample(content)
    }
}
