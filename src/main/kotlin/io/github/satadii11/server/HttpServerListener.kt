package io.github.satadii11.server

interface HttpServerListener {
    fun createGetResponse(): HttpResponse
    fun createPostResponse(data: Map<String, String>): HttpResponse
}