package it.bennes.jsonSchemaGenerator

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val port = (System.getenv("PORT") ?: "8080").toInt()
    val server = embeddedServer(Netty, port = port, module = Application::module)
    server.start(wait = true)
}
