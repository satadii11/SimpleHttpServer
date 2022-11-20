package io.github.satadii11.server

enum class HttpMethod {
    GET,
    POST;
    companion object {
        fun from(methodString: String?) = when(methodString?.lowercase()) {
            "get" -> GET
            "post" -> POST
            else -> null
        }
    }
}