package io.github.satadii11.server

private const val LINE_BREAK = "\r\n"

enum class HttpResponseStatusCode(val code: Int, val reasonPhrase: String) {
    OK(200, "OK"),
    BAD_REQUEST(400, "Bad Request"),
    SERVER_ERROR(500, "Internal Server Error");

    val statusLine get() = "$code $reasonPhrase"
}

data class HttpResponse(
    val statusCode: HttpResponseStatusCode,
    val message: String? = null,
    var version: String? = null
) {
    private val _headers = mutableMapOf<String, String>()
    val headers: Map<String, String> get() = _headers

    init {
        putGeneralHeaders()
    }

    fun getResponse(): String {
        return StringBuilder().apply {
            append(version)
            append(" ")
            append(statusCode.statusLine)
            append(LINE_BREAK)
            append(generateHeadersMessage())
            append(LINE_BREAK)
            append(LINE_BREAK)
            message?.let { append(message) }
        }.toString()
    }

    private fun putGeneralHeaders() {
        _headers.apply {
            put("Content-Type", "text/plain")
            put("Content-Length", (message?.toByteArray()?.size ?: 0).toString())
            put("Connection", "keep-alive")
        }
    }

    private fun generateHeadersMessage(): String {
        return headers.map { "${it.key}: ${it.value}" }.joinToString(LINE_BREAK)
    }

    companion object {
        fun ok(message: String?) = HttpResponse(HttpResponseStatusCode.OK, message)
        fun badRequest(message: String?) = HttpResponse(HttpResponseStatusCode.BAD_REQUEST, message)
        fun serverError(message: String?) = HttpResponse(HttpResponseStatusCode.SERVER_ERROR, message)
    }
}
