package it.bennes.jsonSchemaGenerator

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.logging.*

object HttpClient {
    val client = HttpClient(CIO) {
        followRedirects = false
        BrowserUserAgent()

        install(Logging) {
            level = LogLevel.INFO
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
        }
        install(HttpCache)
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 3)
            exponentialDelay()
        }
    }
}
