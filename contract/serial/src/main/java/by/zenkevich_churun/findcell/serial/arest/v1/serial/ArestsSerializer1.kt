package by.zenkevich_churun.findcell.serial.arest.v1.serial

import by.zenkevich_churun.findcell.entity.entity.LightArest
import by.zenkevich_churun.findcell.entity.response.CreateOrUpdateArestResponse
import by.zenkevich_churun.findcell.serial.arest.serial.ArestsSerializer
import by.zenkevich_churun.findcell.serial.arest.v1.pojo.*
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
        when(response) {
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

    override fun serialize(
        prisonerId: Int,
        passwordHash: ByteArray,
        arestIds: Collection<Int>
    ): String {

        val pojo = ArestIdsPojo1()
        pojo.prisonerId   = prisonerId
        pojo.passwordHash = passwordHash
        pojo.arestIds     = arestIds.toList()

        return ProtocolUtil.toJson(pojo, 192)
    }


    companion object {
        private const val APPROX_BYTES_PER_AREST = 73
    }
}