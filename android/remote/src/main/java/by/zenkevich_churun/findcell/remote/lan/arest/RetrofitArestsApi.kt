package by.zenkevich_churun.findcell.remote.lan.arest

import by.zenkevich_churun.findcell.core.api.arest.ArestsApi
import by.zenkevich_churun.findcell.core.api.arest.CreateOrUpdateArestResponse
import by.zenkevich_churun.findcell.entity.LightArest
import java.util.Calendar


class RetrofitArestsApi: ArestsApi {

    override fun create(
        prisonerId: Int,
        passwordHash: ByteArray,
        start: Calendar,
        end: Calendar
    ): CreateOrUpdateArestResponse {
        TODO("Not yet implemented")
    }

    override fun get(
        prisonerId: Int,
        passwordHash: ByteArray
    ): List<LightArest> {
        TODO("Not yet implemented")
    }

    override fun update(
        prisonerId: Int,
        passwordHash: ByteArray,
        id: Int,
        newStart: Calendar,
        newEnd: Calendar
    ): CreateOrUpdateArestResponse {
        TODO("Not yet implemented")
    }

    override fun delete(
        prisonerId: Int,
        passwordHash: ByteArray,
        id: Int ) {
        TODO("Not yet implemented")
    }
}