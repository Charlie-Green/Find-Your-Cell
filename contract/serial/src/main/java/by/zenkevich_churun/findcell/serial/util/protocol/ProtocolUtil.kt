package by.zenkevich_churun.findcell.serial.util.protocol

import by.zenkevich_churun.findcell.serial.common.v1.CommonContract1
import com.google.gson.Gson
import com.google.gson.stream.JsonWriter
import java.io.*


internal object ProtocolUtil {

    fun toJson(
        pojo: Any,
        initialBufferSize: Int
    ): String {

        val ostream = ByteArrayOutputStream(initialBufferSize)
        val streamWriter = OutputStreamWriter(ostream, CommonContract1.ENCODING)
        val jsonWriter = JsonWriter(streamWriter)

        Gson().toJson(pojo, pojo.javaClass, jsonWriter)
        jsonWriter.flush()

        return String(ostream.toByteArray(), CommonContract1.ENCODING)
    }


    fun <T> fromJson(
        input: InputStream,
        type: Class<T>
    ): T {

        val reader = InputStreamReader(input, CommonContract1.ENCODING)
        return Gson().fromJson(reader, type)
    }
}