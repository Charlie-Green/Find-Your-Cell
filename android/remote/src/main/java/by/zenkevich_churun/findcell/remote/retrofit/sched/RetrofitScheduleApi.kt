package by.zenkevich_churun.findcell.remote.retrofit.sched

import by.zenkevich_churun.findcell.core.api.sched.ScheduleApi
import by.zenkevich_churun.findcell.entity.entity.Schedule
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitApisUtil
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitHolder
import by.zenkevich_churun.findcell.serial.sched.serial.ScheduleDeserializer
import by.zenkevich_churun.findcell.serial.util.protocol.Base64Util
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetrofitScheduleApi @Inject constructor(
    private val retrofitHolder: RetrofitHolder
): ScheduleApi {

    override fun get(
        prisonerId: Int,
        passwordHash: ByteArray,
        arestId: Int
    ): Schedule {

        val service = retrofit.create(ScheduleService::class.java)
        val passwordBase64 = Base64Util.encode(passwordHash)

        val response = service
            .get(1, arestId, passwordBase64)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())

        val responseStream = response.body()!!.byteStream()
        val pojo = ScheduleDeserializer
            .forVersion(1)
            .deserialize(responseStream)

        // These properties are null (not included in the response),
        // but they are needed for mapping. So, initialize them:
        pojo.passwordBase64 = passwordBase64
        pojo.arestId = arestId

        return TODO("Map to Schedule")
    }

    override fun update(
        prisonerId: Int,
        passwordHash: ByteArray,
        schedule: Schedule) {

    }

    override fun addCell(
        prisonerId: Int,
        passwordHash: ByteArray,
        jailId: Int,
        cellNumber: Short ) {
        TODO("")
    }

    override fun updateCell(
        prisonerId: Int,
        passwordHash: ByteArray,
        oldJailId: Int,
        oldCellNumber: Short,
        newJailId: Int,
        newCellNumber: Short ) {
        TODO("")
    }

    override fun deleteCell(
        prisonerId: Int,
        passwordHash: ByteArray,
        jailId: Int,
        cellNumber: Short ) {
        TODO("")
    }


    private val retrofit
        get() = retrofitHolder.retrofit
}