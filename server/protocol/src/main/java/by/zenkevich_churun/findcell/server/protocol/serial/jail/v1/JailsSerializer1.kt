package by.zenkevich_churun.findcell.server.protocol.serial.jail.v1

import by.zenkevich_churun.findcell.entity.entity.Jail
import by.zenkevich_churun.findcell.serial.common.v1.CommonContract1
import by.zenkevich_churun.findcell.server.protocol.serial.jail.abstr.JailsSerializer
import by.zenkevich_churun.findcell.serial.jail.v1.pojo.JailPojo1
import by.zenkevich_churun.findcell.serial.jail.v1.pojo.JailsListPojo1
import com.google.gson.Gson
import com.google.gson.stream.JsonWriter
import java.io.ByteArrayOutputStream
import java.io.PrintWriter


internal class JailsSerializer1: JailsSerializer {

    override fun serialize(jails: List<Jail>): String {

        val ostream = ByteArrayOutputStream(4 + 46*jails.size)
        val printWriter = PrintWriter(ostream, false, CommonContract1.ENCODING)
        val jsonWriter = JsonWriter(printWriter)

        val listPojo = JailsListPojo1.wrap(jails)

        // TODO: Map to JailsListPojo?
        Gson().toJson(listPojo, JailsListPojo1::class.java, jsonWriter)
        jsonWriter.flush()

        return String(ostream.toByteArray(), CommonContract1.ENCODING)
    }
}