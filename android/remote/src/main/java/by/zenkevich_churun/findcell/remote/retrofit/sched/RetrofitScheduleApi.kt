package by.zenkevich_churun.findcell.remote.retrofit.sched

import by.zenkevich_churun.findcell.core.api.sched.ScheduleApi
import by.zenkevich_churun.findcell.core.api.sched.SchedulePropertiesAccessor
import by.zenkevich_churun.findcell.entity.entity.Schedule
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitApisUtil
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitHolder
import by.zenkevich_churun.findcell.remote.retrofit.sched.entity.DeserializedSchedule
import by.zenkevich_churun.findcell.serial.sched.serial.ScheduleDeserializer
import by.zenkevich_churun.findcell.serial.sched.v1.pojo.CellEntryPojo1
import by.zenkevich_churun.findcell.serial.util.protocol.Base64Util
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetrofitScheduleApi @Inject constructor(
    private val retrofitHolder: RetrofitHolder,
    private val props: SchedulePropertiesAccessor
): ScheduleApi {

    private val service by lazy {
        createService()
    }


    override fun get(
        prisonerId: Int,
        passwordHash: ByteArray,
        arestId: Int
    ): Schedule {

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

        return DeserializedSchedule.from(pojo, props)
    }

    override fun update(
        prisonerId: Int,
        passwordHash: ByteArray,
        schedule: Schedule) {

    }

    override fun addCell(
        arestId: Int,
        passwordHash: ByteArray,
        jailId: Int,
        cellNumber: Short ) {
        TODO("")
    }

    override fun updateCell(
        arestId: Int,
        passwordHash: ByteArray,
        oldJailId: Int,
        oldCellNumber: Short,
        newJailId: Int,
        newCellNumber: Short ) {
        TODO("")
    }

    override fun deleteCell(
        arestId: Int,
        passwordHash: ByteArray,
        jailId: Int,
        cellNumber: Short ) {

        val pojo = CellEntryPojo1()
        pojo.arestId    = arestId
        pojo.jailId     = jailId
        pojo.cellNumber = cellNumber

        val json = Gson().toJson(pojo)
        val mediaType = MediaType.get("application/json")
        val body = RequestBody.create(mediaType, json)

        val response = createService()
            .deleteCell(body)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())
    }


    private fun createService(): ScheduleService {
        return retrofitHolder.retrofit.create(ScheduleService::class.java)
    }
}