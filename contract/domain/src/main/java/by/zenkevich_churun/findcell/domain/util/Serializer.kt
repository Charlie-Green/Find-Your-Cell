package by.zenkevich_churun.findcell.domain.util

import com.google.gson.Gson
import com.google.gson.stream.JsonWriter
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter


/** Utility class to serialize POJOs into JSON. **/
object Serializer {

    fun toJsonString(
        pojo: Any,
        initialBufferSize: Int
    ): String {

        val ostream = ByteArrayOutputStream(initialBufferSize)
        val streamWriter = OutputStreamWriter(ostream, Charsets.UTF_8)
        val jsonWriter = JsonWriter(streamWriter)

        Gson().toJson(pojo, pojo.javaClass, jsonWriter)
        jsonWriter.flush()

        return String(ostream.toByteArray(), Charsets.UTF_8)
    }
}