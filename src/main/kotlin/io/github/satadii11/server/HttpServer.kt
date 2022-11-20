package io.github.satadii11.server

import io.github.satadii11.log.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException

interface HttpServer {
    fun startServer(port: Int)
    fun stopServer()
}

class SimpleHttpServer(
    private val logger: Logger? = null
) : HttpServer {
    private var serverSocket: ServerSocket? = null
    private var isStarted = false

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        Runtime.getRuntime().addShutdownHook(Thread {
            closeServerIfStarted()
        })
    }

    override fun startServer(port: Int) {
        closeServerIfStarted()

        logger?.i("Listening in port: $port")
        serverSocket = ServerSocket(port)
        isStarted = true

        while (true) {
            logger?.i("Waiting for client request")
            val socket = serverSocket?.accept()
            coroutineScope.launch { process(socket) }
        }
    }

    override fun stopServer() {
        isStarted = false
        serverSocket?.close()
        logger?.i("Server is closed")
    }

    private fun process(socket: Socket?) {
        val message = socket?.let { getMessage(it) } ?: return
        logger?.i("Received message:\n$message")

        if (message.isNotEmpty()) {
            val httpRequest = HttpRequest.createFrom(message)
            if (httpRequest.method == HttpMethod.GET) {
                processGetMethod(socket, httpRequest)
            }
        } else {
            processBadRequest(socket)
        }

        socket.close()
    }

    private fun closeServerIfStarted() {
        if (isStarted) {
            logger?.i("Closing the server...")
            coroutineScope.cancel()
            stopServer()
        }
    }

    private fun getMessage(socket: Socket): String? {
        if (socket.isClosed) {
            logger?.i("Something wrong, socket is closed and cannot read the request")
            return null
        }

        return buildString {
            val inputStream = socket.getInputStream()
            var c = inputStream.read()
            while (c >= 0 && c != 0x0a) {
                if (c != 0x0d) {
                    append(c.toChar())
                }
                c = inputStream.read()
            }

            toString()
        }.trim()
    }

    private fun processGetMethod(socket: Socket?, httpRequest: HttpRequest) {
        writeToSocket(socket, HttpResponse(HttpResponseStatusCode.OK, httpRequest.version, "Halo dari Satria"))
    }

    private fun processBadRequest(socket: Socket?) {
        writeToSocket(socket, HttpResponse(HttpResponseStatusCode.BAD_REQUEST, "HTTP/1.1"))
    }

    private fun writeToSocket(socket: Socket?, httpResponse: HttpResponse) {
        if (socket?.isClosed == true) {
            logger?.i("Something wrong, socket is closed and cannot process the request")
            return
        }

        val outputStream = socket?.getOutputStream()
        try {
            outputStream?.run {
                write(httpResponse.getResponse().toByteArray())
                flush()
                logger?.i("Response Message")
                logger?.i(httpResponse.getResponse())
            }
        } catch (exception: SocketException) {
            socket?.close()
        }
    }
}