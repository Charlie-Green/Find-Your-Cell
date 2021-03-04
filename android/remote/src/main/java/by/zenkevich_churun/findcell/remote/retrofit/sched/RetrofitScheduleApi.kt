package by.zenkevich_churun.findcell.remote.retrofit.sched

import by.zenkevich_churun.findcell.core.api.sched.ScheduleApi
import by.zenkevich_churun.findcell.core.api.sched.SchedulePropertiesAccessor
import by.zenkevich_churun.findcell.domain.contract.cellentry.CellEntryPojo
import by.zenkevich_churun.findcell.domain.contract.cellentry.UpdatedCellEntryPojo
import by.zenkevich_churun.findcell.domain.contract.sched.ScheduleFetchedPojo
import by.zenkevich_churun.findcell.domain.contract.sched.ScheduleUpdatedPojo
import by.zenkevich_churun.findcell.domain.entity.Schedule
import by.zenkevich_churun.findcell.domain.simpleentity.SimpleSchedule
import by.zenkevich_churun.findcell.domain.util.*
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitApisUtil
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitHolder
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
        arestId: Int,
        passwordHash: ByteArray,
        arestStart: Long,
        arestEnd: Long
    ): Schedule {

        val passwordBase64 = base64.encode(passwordHash)

        val response = createService()
            .get(1, arestId, passwordBase64)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())

        val pojo = Deserializer.fromJsonStream(
            response.body()!!.byteStream(),
            ScheduleFetchedPojo::class.java
        )

        return SimpleSchedule(
            arestId,
            arestStart,
            arestEnd,
            pojo.cells,
            pojo.periods
        )
    }

    override fun update(
        passwordHash: ByteArray,
        schedule: Schedule ) {

        val passBase64 = base64.encode(passwordHash)
        val pojo = ScheduleUpdatedPojo.from(schedule, passBase64)
        val approxSize = passBase64.length
            + 64*schedule.cells.size
            + 32*schedule.periods.size
        val data = Serializer.toJsonString(pojo, approxSize)

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

        val pojo = CellEntryPojo(
            base64.encode(passwordHash),
            arestId,
            jailId,
            cellNumber
        )

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

        val pojo = UpdatedCellEntryPojo(
            base64.encode(passwordHash),
            arestId,
            oldJailId, oldCellNumber,
            newJailId, newCellNumber
        )
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

        val pojo = CellEntryPojo(
            base64.encode(passwordHash),
            arestId,
            jailId,
            cellNumber
        )
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