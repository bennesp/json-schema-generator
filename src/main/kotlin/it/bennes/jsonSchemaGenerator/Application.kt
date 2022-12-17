package it.bennes.jsonSchemaGenerator

import it.bennes.jsonSchemaGenerator.exceptions.HTTPException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.module() {
    val log = environment.log

    install(CallLogging) {
        disableDefaultColors()
        callIdMdc("call-id")
    }
    install(Compression)
    install(CallId) {
        // set of 23 unambiguous characters
        val dictionary = "cdefhjkmnprtvwxy2345689"
        generate(16, dictionary)
        header(HttpHeaders.XRequestId)
    }
    install(StatusPages) {
        exception<HTTPException> { call, cause ->
            log.warn("HTTPException: ${cause.message}")
            call.respond(cause.statusCode, cause.message)
        }
        exception<Exception> { call, cause ->
            log.error("unhandled exception", cause)
            call.respond(HttpStatusCode.InternalServerError, "internal server error")
        }
    }

    routing {
        main()
    }
}
