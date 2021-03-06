package com.hylamobile.voorhees.server.spring.webmvc

import com.hylamobile.voorhees.jsonrpc.Request
import com.hylamobile.voorhees.jsonrpc.Response
import com.hylamobile.voorhees.jsonrpc.readRequest
import com.hylamobile.voorhees.jsonrpc.writeResponse
import org.springframework.http.InvalidMediaTypeException
import org.springframework.http.MediaType
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val REQ_ATTR_NAME = "jsonrpc:request"

val HttpServletRequest.jsonRequest
    get() = getAttribute(REQ_ATTR_NAME) as Request? ?:
        reader.use { it.readRequest() }.also { setAttribute(REQ_ATTR_NAME, it) }

val HttpServletRequest.realPath
    get() = pathInfo ?: requestURI.substring(contextPath.length)

val HttpServletRequest.contentMediaType
    get() =
        try { MediaType.valueOf(contentType) }
        catch (ex: InvalidMediaTypeException) { null }

val HttpServletRequest.acceptedMediaTypes: List<MediaType>
    get() = MediaType.parseMediaTypes(getHeader("Accept"))

val MediaType.isJsonCompatible
    get() = isCompatibleWith(MediaType.APPLICATION_JSON)

fun <T> HttpServletResponse.send(jsonResponse: Response<T>) {
    contentType = MediaType.APPLICATION_JSON_UTF8_VALUE
    characterEncoding = "UTF-8"
    writer.use { it.writeResponse(jsonResponse) }
}
