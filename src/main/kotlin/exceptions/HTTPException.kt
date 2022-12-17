package exceptions

import io.ktor.http.*

open class HTTPException(val statusCode: HttpStatusCode, override val message: String) : Exception(message)
