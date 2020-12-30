package by.zenkevich_churun.findcell.serial.arest.abstr

import by.zenkevich_churun.findcell.entity.entity.LightArest
import by.zenkevich_churun.findcell.entity.response.CreateOrUpdateArestResponse
import by.zenkevich_churun.findcell.serial.arest.v1.pojo.ArestPojo1
import by.zenkevich_churun.findcell.serial.arest.v1.serial.ArestsDeserializer1
import java.io.InputStream


interface ArestsDeserializer {
    fun deserializeList(input: InputStream): List<LightArest>
    fun deserializeOne(input: InputStream): ArestPojo1
    fun deserializeResponse(input: InputStream): CreateOrUpdateArestResponse


    companion object {

        fun forVersion(v: Int): ArestsDeserializer {
            if(v == 1) {
                return ArestsDeserializer1()
            }
            throw IllegalArgumentException("Unknown version $v")
        }
    }
}