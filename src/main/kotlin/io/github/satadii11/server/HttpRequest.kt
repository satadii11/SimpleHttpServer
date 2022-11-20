package io.github.satadii11.server

data class HttpRequest(
    val method: HttpMethod?,
    val route: String,
    val version: String,
    val headers: Map<String, String>,
    val body: Map<String, String>
) {
    companion object {
        private const val DEFAULT_ROUTE = "/"
        private const val DEFAULT_VERSION = "HTTP/1.1"

        fun createFrom(message: String): HttpRequest {
            val lines = message.split("\n")

            val firstLine = lines.getOrNull(0)?.split(" ")
            val method = HttpMethod.from(firstLine?.getOrNull(0))
            val route = firstLine?.getOrNull(1) ?: DEFAULT_ROUTE
            val version = firstLine?.getOrNull(2) ?: DEFAULT_VERSION

            val headers = mutableMapOf<String, String>()
            lines.subList(1, lines.size).forEach {
                val header = it.split(": ")
                if (header.size != 2) {
                    return@forEach
                }

                headers[header[0]] = header[1]
            }
            return HttpRequest(method, route, version, headers, mapOf())
        }
    }
}
