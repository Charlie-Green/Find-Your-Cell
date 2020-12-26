package by.zenkevich_churun.findcell.remote.lan.common

import java.net.HttpURLConnection


object RetrofitApisUtil {

    fun assertResponseCode(code: Int) {
        if(code != HttpURLConnection.HTTP_OK) {
            throw Error("Unexpected response code $code")
        }
    }
}