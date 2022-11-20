import com.google.gson.Gson
import io.github.satadii11.log.ConsoleLogger
import io.github.satadii11.server.HttpResponse
import io.github.satadii11.server.HttpServerListener
import io.github.satadii11.server.SimpleHttpServer
import java.text.SimpleDateFormat
import java.util.*

fun main() {
    val gson = Gson()
    with(SimpleHttpServer(ConsoleLogger())) {
        listener = object: HttpServerListener {
            override fun createGetResponse(): HttpResponse {
                val currentTime = SimpleDateFormat("dd/MMM/yyyy hh:mm:ss").format(Date())
                val message = "Halo for your information, now is $currentTime"
                return HttpResponse.ok(message)
            }

            override fun createPostResponse(data: Map<String, String>): HttpResponse {
                val response = gson.toJson(data).toString()
                val base64 = Base64.getEncoder().encode(response.toByteArray())
                return HttpResponse.ok(base64.toString())
            }
        }
        startServer(9099)
    }
}