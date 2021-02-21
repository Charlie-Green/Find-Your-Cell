package by.zenkevich_churun.findcell.serial.arest.serial

import by.zenkevich_churun.findcell.entity.entity.LightArest
import by.zenkevich_churun.findcell.entity.response.CreateOrUpdateArestResponse
import by.zenkevich_churun.findcell.serial.arest.v1.serial.ArestsSerializer1
import by.zenkevich_churun.findcell.serial.common.abstr.Base64Coder


interface ArestsSerializer {
    fun serialize(arests: List<LightArest>): String

    fun serialize(
        arest: LightArest,
        prisonerId: Int,
        passwordHash: ByteArray
    ): String

    fun sertialize(response: CreateOrUpdateArestResponse): String

    fun serialize(
        prisonerId: Int,
        passwordHash: ByteArray,
        arestIds: Collection<Int>
    ): String


    companion object {

        fun forVersion(
            v: Int,
            base64: Base64Coder
        ): ArestsSerializer {

            if(v == 1) {
                return ArestsSerializer1(base64)
            }
            throw IllegalArgumentException("Unknown version $v")
        }
    }
}