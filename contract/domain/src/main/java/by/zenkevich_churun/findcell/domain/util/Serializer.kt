package by.zenkevich_churun.findcell.domain.util

import com.google.gson.Gson


/** Utility class to serialize POJOs into JSON. **/
object Serializer {

    fun toJsonString(
        pojo: Any
    ): String = Gson().toJson(pojo)
}