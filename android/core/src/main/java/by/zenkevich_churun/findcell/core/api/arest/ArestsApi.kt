package by.zenkevich_churun.findcell.core.api.arest

import by.zenkevich_churun.findcell.core.entity.arest.LightArest
import java.util.Calendar


interface ArestsApi {

    fun get(
        prisonerId: Int,
        passwordHash: ByteArray
    ): List<LightArest>

    /** @return This method never returns [CreateOrUpdateArestResponse.NetworkError].
      *         Network errors have to be try-catched by the caller. **/
    fun create(
        prisonerId: Int,
        passwordHash: ByteArray,
        start: Calendar,
        end: Calendar
    ): CreateOrUpdateArestResponse

    fun update(
        prisonerId: Int,
        passwordHash: ByteArray,
        id: Int,
        newStart: Calendar,
        newEnd: Calendar
    ): CreateOrUpdateArestResponse

    fun delete(
        prisonerId: Int,
        passwordHash: ByteArray,
        id: Int
    )
}