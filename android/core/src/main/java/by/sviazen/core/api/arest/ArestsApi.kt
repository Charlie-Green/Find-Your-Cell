package by.sviazen.core.api.arest

import by.sviazen.domain.entity.LightArest
import by.sviazen.domain.contract.arest.CreateOrUpdateArestResponse


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
        start: Long,
        end: Long
    ): CreateOrUpdateArestResponse

    fun update(
        prisonerId: Int,
        passwordHash: ByteArray,
        id: Int,
        newStart: Long,
        newEnd: Long
    ): CreateOrUpdateArestResponse

    fun delete(
        prisonerId: Int,
        passwordHash: ByteArray,
        ids: Collection<Int>
    )
}