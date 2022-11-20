package io.github.satadii11.server

enum class HttpMethod {
    GET;

    companion object {
        fun from(methodString: String?) = when(methodString?.lowercase()) {
            "get" -> GET
            else -> null
        }
    }
}