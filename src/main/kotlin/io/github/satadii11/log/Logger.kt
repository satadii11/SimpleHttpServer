package io.github.satadii11.log

interface Logger {
    fun i(message: String?)
}

class ConsoleLogger : Logger {
    override fun i(message: String?) {
        message?.let { println(it) }
    }
}