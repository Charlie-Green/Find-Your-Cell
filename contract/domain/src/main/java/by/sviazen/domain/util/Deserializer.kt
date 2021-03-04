package by.sviazen.domain.util

import com.google.gson.Gson
import java.io.InputStream
import java.io.InputStreamReader


/** Utility class to deserialize POJOs from JSON. **/
object Deserializer {

    fun <T> fromJsonStream(
        istream: InputStream,
        targetClass: Class<T>
    ): T = InputStreamReader(istream, Charsets.UTF_8).use { reader ->
        Gson().fromJson(reader, targetClass)
    }
}