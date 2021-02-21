package by.zenkevich_churun.findcell.remote.retrofit.sched

import by.zenkevich_churun.findcell.core.api.sched.ScheduleApi
import by.zenkevich_churun.findcell.core.api.sched.SchedulePropertiesAccessor
import by.zenkevich_churun.findcell.entity.entity.Schedule
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitApisUtil
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitHolder
import by.zenkevich_churun.findcell.remote.retrofit.sched.entity.DeserializedSchedule
import by.zenkevich_churun.findcell.serial.common.abstr.Base64Coder
import by.zenkevich_churun.findcell.serial.sched.serial.ScheduleDeserializer
import by.zenkevich_churun.findcell.serial.sched.serial.ScheduleSerializer
import by.zenkevich_churun.findcell.serial.sched.v1.pojo.CellEntryPojo1
import by.zenkevich_churun.findcell.serial.sched.v1.pojo.LightSchedulePojo1
import by.zenkevich_churun.findcell.serial.sched.v1.pojo.TwoCellEntriesPojo1
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetrofitScheduleApi @Inject constructor(
    private val retrofitHolder: RetrofitHolder,
    private val props: SchedulePropertiesAccessor,
    private val base64: Base64Coder
): ScheduleApi {

    override fun get(
        prisonerId: Int,
        passwordHash: ByteArray,
        arestId: Int
    ): Schedule {

        val passwordBase64 = base64.encode(passwordHash)

        val response = createService()
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
        schedule: Schedule ) {

        val passBase64 = base64.encode(passwordHash)
        val pojo = LightSchedulePojo1.from(schedule, passBase64)
        val data = ScheduleSerializer
            .forVersion(1)
            .serializeLight(pojo)

        val mediaType = MediaType.get("application/json")
        val body = RequestBody.create(mediaType, data)
        val response = createService()
            .save(body)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())
    }


    override fun addCell(
        arestId: Int,
        passwordHash: ByteArray,
        jailId: Int,
        cellNumber: Short ) {

        val pojo = CellEntryPojo1()
        pojo.passwordBase64 = base64.encode(passwordHash)
        pojo.arestId = arestId
        pojo.jailId = jailId
        pojo.cellNumber = cellNumber

        val body = RetrofitApisUtil.jsonBody(pojo, passwordHash.size + 96)

        val response = createService()
            .addCell(body)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())
    }

    override fun updateCell(
        arestId: Int, passwordHash: ByteArray,
        oldJailId: Int, oldCellNumber: Short,
        newJailId: Int, newCellNumber: Short ) {

        val pojo = TwoCellEntriesPojo1()
        pojo.arestId = arestId
        pojo.passwordBase64 = base64.encode(passwordHash)
        pojo.oldJailId     = oldJailId
        pojo.oldCellNumber = oldCellNumber
        pojo.newJailId     = newJailId
        pojo.newCellNumber = newCellNumber

        val body = RetrofitApisUtil.jsonBody(pojo, passwordHash.size + 128)

        val response = createService()
            .updateCell(body)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())
    }

    override fun deleteCell(
        arestId: Int,
        passwordHash: ByteArray,
        jailId: Int,
        cellNumber: Short ) {

        val pojo = CellEntryPojo1()
        pojo.passwordBase64 = base64.encode(passwordHash)
        pojo.arestId        = arestId
        pojo.jailId         = jailId
        pojo.cellNumber     = cellNumber

        val body = RetrofitApisUtil.jsonBody(pojo, passwordHash.size + 96)

        val response = createService()
            .deleteCell(body)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())
    }


    private fun createService(): ScheduleService {
        return retrofitHolder.retrofit.create(ScheduleService::class.java)
    }
}