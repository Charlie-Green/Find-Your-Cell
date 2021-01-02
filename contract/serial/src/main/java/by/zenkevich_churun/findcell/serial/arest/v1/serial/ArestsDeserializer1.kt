package by.zenkevich_churun.findcell.serial.arest.v1.serial

import by.zenkevich_churun.findcell.entity.entity.LightArest
import by.zenkevich_churun.findcell.entity.response.CreateOrUpdateArestResponse
import by.zenkevich_churun.findcell.serial.arest.pojo.*
import by.zenkevich_churun.findcell.serial.arest.serial.ArestsDeserializer
import by.zenkevich_churun.findcell.serial.arest.v1.pojo.*
import by.zenkevich_churun.findcell.serial.common.v1.CommonContract1
import by.zenkevich_churun.findcell.serial.util.protocol.ProtocolUtil
import java.io.InputStream


internal class ArestsDeserializer1: ArestsDeserializer {

    override fun deserializeList(input: InputStream): List<LightArest> {
        return ProtocolUtil
            .fromJson(input, ArestsListPojo1::class.java)
            .arests
    }

    override fun deserializeOne(input: InputStream): ArestPojo {
        return ProtocolUtil.fromJson(input, ArestPojo1::class.java)
    }

    override fun deserializeResponse(
        input: InputStream
    ): CreateOrUpdateArestResponse {

        val data = String(input.readBytes(), CommonContract1.ENCODING)
        val id = data.substring(1).toInt()

        return when(val prefix = data[0]) {
            'S' -> CreateOrUpdateArestResponse.Success(id)
            'I' -> CreateOrUpdateArestResponse.ArestsIntersect(id)
            else -> throw IllegalArgumentException("Unknown prefix $prefix")
        }
    }

    override fun deserializeIds(input: InputStream): ArestIdsPojo {
        return ProtocolUtil.fromJson(input, ArestIdsPojo1::class.java)
    }
}