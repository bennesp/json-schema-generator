package it.bennes.jsonSchemaGenerator

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.plugins.*

object HttpClient {
    private val client = HttpClient(CIO) {
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

    suspend fun get(url: String): HttpResponse {
        return try {
            val response = client.get(url)
            if (response.status.value !in 200..299) {
                throw it.bennes.jsonSchemaGenerator.exceptions.BadRequestException("url returned invalid status code ${response.status.value} (not 2xx)")
            }
            response
        } catch (e: HttpRequestTimeoutException) {
            throw BadRequestException("timeout while fetching url")
        }
    }
}
