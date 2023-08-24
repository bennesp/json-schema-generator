package it.bennes.jsonSchemaGenerator

import io.ktor.util.logging.*
import it.bennes.jsonSchemaGenerator.model.Request
import org.slf4j.MDC
import org.slf4j.event.Level

object Utils {
    fun logRequest(logger: Logger, r: Request, message: String, level: String = "info") {
        // Add context to logs
        MDC.put("url", r.url)
        MDC.put("data (length)", (r.data?.length ?: 0).toString())
        MDC.put("inputFormat", r.inputFormat)
        MDC.put("outputFormat", r.outputFormat)
        MDC.put("encoding", r.encoding)
        MDC.put("generate", r.generate.toString())
        MDC.put("selector", r.selector)

        val log = logger.atLevel(Level.valueOf(level.uppercase()))
        log.log(message)

        MDC.remove("url")
        MDC.remove("data (length)")
        MDC.remove("inputFormat")
        MDC.remove("outputFormat")
        MDC.remove("encoding")
        MDC.remove("generate")
        MDC.remove("selector")
    }
}
