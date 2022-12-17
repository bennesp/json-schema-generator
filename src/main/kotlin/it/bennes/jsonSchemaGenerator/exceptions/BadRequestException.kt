package it.bennes.jsonSchemaGenerator.exceptions

import io.ktor.http.*

class BadRequestException(override val message: String) : HTTPException(HttpStatusCode.BadRequest, message)
