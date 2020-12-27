package by.zenkevich_churun.findcell.remote.retrofit.common

import java.io.IOException
import java.net.HttpURLConnection


object RetrofitApisUtil {

    fun assertResponseCode(code: Int) {
        if(code == HttpURLConnection.HTTP_INTERNAL_ERROR) {
            throw IOException("Server error")
        }

        if(code != HttpURLConnection.HTTP_OK) {
            throw Error("Unexpected response code $code")
        }
    }
}