import exceptions.HTTPException
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
        // TODO customize format to use json
        /*
        {"level":"info","msg":"I have to go...","time":"2021-07-03T10:18:10Z"}
        {"level":"info","msg":"Stopping server gracefully","time":"2021-07-03T10:18:10Z"}
        {"entryPointName":"web","level":"debug","msg":"Waiting 10s seconds before killing connections.","time":"2021-07-03T10:18:10Z"}
        {"entryPointName":"web","level":"error","msg":"accept tcp [::]:80: use of closed network connection","time":"2021-07-03T10:18:10Z"}
        */
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
