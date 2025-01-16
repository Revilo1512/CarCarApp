import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.ConcurrentHashMap

class CookieInterceptor : Interceptor {
    private val cookieStore = ConcurrentHashMap<String, MutableList<String>>()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url: HttpUrl = request.url() // Get the HttpUrl object
        val host = url.host() // Extract the host as a String

        // Add cookies to the request if available
        val cookies = cookieStore[host]?.joinToString("; ")
        val newRequest = if (!cookies.isNullOrEmpty()) {
            request.newBuilder()
                .addHeader("Cookie", cookies)
                .build()
        } else {
            request
        }

        // Proceed with the request
        val response = chain.proceed(newRequest)

        // Extract cookies from the response headers
        val responseCookies = response.headers("Set-Cookie")
        if (responseCookies.isNotEmpty()) {
            cookieStore[host] = responseCookies.toMutableList()
        }

        println("Request URL: $url")
        println("Host: $host")
        println("Cookies being sent: $cookies")
        println("Cookies received: $responseCookies")

        return response
    }

    // Method to clear the cookies when logging out
    fun clearCookies() {
        cookieStore.clear() // Clear the cookies
        println("Cookies cleared")
    }
}