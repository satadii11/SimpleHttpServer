import io.github.satadii11.log.ConsoleLogger
import io.github.satadii11.server.SimpleHttpServer

fun main() {
    SimpleHttpServer(ConsoleLogger()).startServer(9099)
}