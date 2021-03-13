package by.sviazen.remote.retrofit.common

import by.sviazen.domain.util.Serializer
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.IOException
import java.net.HttpURLConnection


object RetrofitApisUtil {

    fun assertResponseCode(code: Int) {
        if(code == HttpURLConnection.HTTP_INTERNAL_ERROR) {
            throw IOException("Server error")
        }

        if(code == 503) {
            throw IOException("Code 503. The server is probably shutdown")
        }

        if(code != HttpURLConnection.HTTP_OK) {
            throw RuntimeException("Unexpected response code $code")
        }
    }


    fun jsonBody(
        pojo: Any,
        initialBufferSize: Int
    ): RequestBody {

        val mediaType = MediaType.get("application/json")
        val json = Serializer.toJsonString(pojo, initialBufferSize)
        return RequestBody.create(mediaType, json)
    }
}