package by.zenkevich_churun.findcell.serial.arest.v1.serial

import by.zenkevich_churun.findcell.entity.entity.LightArest
import by.zenkevich_churun.findcell.entity.response.CreateOrUpdateArestResponse
import by.zenkevich_churun.findcell.serial.arest.abstr.ArestsSerializer
import by.zenkevich_churun.findcell.serial.arest.v1.pojo.ArestPojo1
import by.zenkevich_churun.findcell.serial.arest.v1.pojo.ArestsListPojo1
import by.zenkevich_churun.findcell.serial.util.protocol.ProtocolUtil


internal class ArestsSerializer1: ArestsSerializer {

    override fun serialize(arests: List<LightArest>): String {
        val listPojo = ArestsListPojo1.wrap(arests)
        return ProtocolUtil.toJson(listPojo, 4 + APPROX_BYTES_PER_AREST*arests.size)
    }

    override fun serialize(
        arest: LightArest,
        prisonerId: Int,
        passwordHash: ByteArray
    ): String {
        val pojo = ArestPojo1.from(arest, prisonerId, passwordHash)
        return ProtocolUtil.toJson(pojo, APPROX_BYTES_PER_AREST)
    }

    override fun sertialize(response: CreateOrUpdateArestResponse): String {
        return when(response) {
            is CreateOrUpdateArestResponse.ArestsIntersect -> {
                return "I${response.intersectedId}"
            }

            is CreateOrUpdateArestResponse.Success -> {
                return "S${response.arestId}"
            }

            else -> {
                throw IllegalArgumentException(
                    "Unknown response ${response.javaClass.simpleName}"
                )
            }
        }
    }


    companion object {
        private const val APPROX_BYTES_PER_AREST = 73
    }
}