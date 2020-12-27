package by.zenkevich_churun.findcell.remote.retrofit.common

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHolder {

    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.167:8080/")  // TODO: Replace with real server domain name
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}