package by.zenkevich_churun.findcell.serial.arest.v1.serial

import by.zenkevich_churun.findcell.entity.entity.LightArest
import by.zenkevich_churun.findcell.serial.arest.abstr.ArestsDeserializer
import by.zenkevich_churun.findcell.serial.arest.v1.pojo.ArestPojo1
import by.zenkevich_churun.findcell.serial.arest.v1.pojo.ArestsListPojo1
import by.zenkevich_churun.findcell.serial.util.protocol.ProtocolUtil
import com.google.gson.Gson
import java.io.InputStream
import java.io.InputStreamReader


internal class ArestsDeserializer1: ArestsDeserializer {

    override fun deserializeList(input: InputStream): List<LightArest> {
        return ProtocolUtil
            .fromJson(input, ArestsListPojo1::class.java)
            .arests
    }

    override fun deserializeOne(input: InputStream): ArestPojo1 {
        return ProtocolUtil.fromJson(input, ArestPojo1::class.java)
    }
}