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
    val version: String,
    val message: String
) {
    private val _headers = mutableMapOf<String, String>()
    val headers: Map<String, String> get() = _headers

    init {
        putGeneralHeaders()
    }

    fun getResponse(): String {
//        return "HTTP/1.1 200 OK\r\n\r\n"
        return StringBuilder().apply {
            append(version)
            append(" ")
            append(statusCode.statusLine)
            append(LINE_BREAK)
            append(generateHeadersMessage())
            append(LINE_BREAK)
            append(LINE_BREAK)
            append(message)
        }.toString()
    }

    private fun putGeneralHeaders() {
        _headers.apply {
            put("Content-Type", "text/plain")
            put("Content-Length", message.toByteArray().size.toString())
            put("Connection", "keep-alive")
        }
    }

    private fun generateHeadersMessage(): String {
        return headers.map { "${it.key}: ${it.value}" }.joinToString(LINE_BREAK)
    }
}
