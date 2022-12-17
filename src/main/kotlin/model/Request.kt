package model

import encoding.Encoding
import exceptions.BadRequestException
import format.Format
import io.ktor.http.*
import org.apache.commons.validator.routines.UrlValidator
import java.net.InetAddress
import java.net.URI


data class Request(
    val url: String?,
    val data: String?,
    val inputFormat: String,
    val outputFormat: String,
    val encoding: String,
) {
    companion object {
        private val urlValidator = UrlValidator(arrayOf("http", "https"))

        fun fromParameters(p: Parameters): Request {
            if ((!("url" in p || "data" in p)) || ("url" in p && "data" in p)) {
                throw BadRequestException("either url or data must be provided")
            }

            val url = if ("url" in p) {
                val tmpUrl = p["url"]!!
                if (!urlValidator.isValid(tmpUrl)) {
                    throw BadRequestException("invalid url")
                }
                val uri = URI(tmpUrl)
                val address = InetAddress.getByName(uri.host)
                if (
                    address.isAnyLocalAddress
                    || address.isLoopbackAddress
                    || address.isLinkLocalAddress
                    || address.isSiteLocalAddress
                    || address.isMulticastAddress
                ) {
                    throw BadRequestException("url must not be localhost")
                }
                uri.toString()
            } else null

            val data = if ("data" in p) {
                p["data"]!!.ifBlank {
                    throw BadRequestException("data must not be blank")
                }
            } else null

            val inputFormat = if ("input" in p) {
                p["input"]!!
            } else {
                Format.Yaml.name
            }

            val outputFormat = if ("output" in p) {
                p["output"]!!
            } else {
                Format.Json.name
            }

            val encoding = if ("encoding" in p) {
                p["encoding"]!!
            } else {
                Encoding.Nop.name
            }

            return Request(
                url = url,
                data = data,
                inputFormat = inputFormat,
                outputFormat = outputFormat,
                encoding = encoding,
            )
        }
    }
}
