package by.zenkevich_churun.findcell.remote.retrofit.common

import by.zenkevich_churun.findcell.domain.util.Serializer
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.IOException
import java.net.HttpURLConnection


object RetrofitApisUtil {

    fun assertResponseCode(code: Int) {
        if(code == HttpURLConnection.HTTP_INTERNAL_ERROR) {
            throw IOException("Server error")
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