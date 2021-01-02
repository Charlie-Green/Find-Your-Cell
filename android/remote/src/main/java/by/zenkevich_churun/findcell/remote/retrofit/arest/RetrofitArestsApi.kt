package by.zenkevich_churun.findcell.remote.retrofit.arest

import by.zenkevich_churun.findcell.core.api.arest.ArestsApi
import by.zenkevich_churun.findcell.entity.response.CreateOrUpdateArestResponse
import by.zenkevich_churun.findcell.entity.entity.LightArest
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitApisUtil
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitHolder
import by.zenkevich_churun.findcell.serial.arest.serial.ArestsDeserializer
import by.zenkevich_churun.findcell.serial.arest.serial.ArestsSerializer
import by.zenkevich_churun.findcell.serial.arest.v1.pojo.ArestPojo1
import by.zenkevich_churun.findcell.serial.util.protocol.Base64Util
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetrofitArestsApi @Inject constructor(
    private val retrofitHolder: RetrofitHolder
): ArestsApi {

    override fun create(
        prisonerId: Int,
        passwordHash: ByteArray,
        start: Calendar,
        end: Calendar
    ): CreateOrUpdateArestResponse {

        val service = retrofit.create(ArestsService::class.java)

        val arest = ArestPojo1()
        arest.startMillis = start.timeInMillis
        arest.endMillis   = end.timeInMillis

        val json = ArestsSerializer
            .forVersion(1)
            .serialize(arest, prisonerId, passwordHash)
        val mediaType = MediaType.get("application/json")
        val requestBody = RequestBody.create(mediaType, json)

        val response = service
            .create(requestBody)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())

        return ArestsDeserializer
            .forVersion(1)
            .deserializeResponse(response.body()!!.byteStream())
    }

    override fun get(
        prisonerId: Int,
        passwordHash: ByteArray
    ): List<LightArest> {

        val passwordBase64 = Base64Util.encode(passwordHash)

        val service = retrofit.create(ArestsService::class.java)
        val response = service
            .get(1, prisonerId, passwordBase64)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())

        return ArestsDeserializer
            .forVersion(1)
            .deserializeList(response.body()!!.byteStream())
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
        ids: Collection<Int> ) {

        val service = retrofit.create(ArestsService::class.java)

        val json = ArestsSerializer
            .forVersion(1)
            .serialize(prisonerId, passwordHash, ids)
        val mediaType = MediaType.get("application/json")
        val request = RequestBody.create(mediaType, json)

        val response = service
            .delete(request)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())
    }


    private val retrofit
        get() = retrofitHolder.retrofit
}