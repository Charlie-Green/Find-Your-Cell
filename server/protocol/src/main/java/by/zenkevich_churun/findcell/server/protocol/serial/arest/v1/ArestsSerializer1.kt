package by.zenkevich_churun.findcell.server.protocol.serial.arest.v1

import by.zenkevich_churun.findcell.entity.entity.LightArest
import by.zenkevich_churun.findcell.serial.common.v1.CommonContract1
import by.zenkevich_churun.findcell.server.protocol.serial.arest.abstr.ArestsSerializer
import by.zenkevich_churun.findcell.serial.arest.v1.pojo.ArestPojo1
import by.zenkevich_churun.findcell.serial.arest.v1.pojo.ArestsListPojo1
import com.google.gson.Gson
import com.google.gson.stream.JsonWriter
import java.io.ByteArrayOutputStream
import java.io.PrintWriter


internal class ArestsSerializer1: ArestsSerializer {

    override fun serialize(arests: List<LightArest>): String {
        val ostream = ByteArrayOutputStream(4 + 73*arests.size)
        val printWriter = PrintWriter(ostream, false, CommonContract1.ENCODING)
        val jsonWriter = JsonWriter(printWriter)

        // TODO: Do I have to wrap list into a POJO?
        val arestPojos = arests.map { arest ->
            ArestPojo1.from(arest)
        }

        Gson().toJson(arestPojos, ArestsListPojo1::class.java, jsonWriter)
        return String(ostream.toByteArray(), CommonContract1.ENCODING)
    }
}