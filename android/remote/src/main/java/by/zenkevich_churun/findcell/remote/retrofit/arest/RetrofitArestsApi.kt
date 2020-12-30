package by.zenkevich_churun.findcell.remote.retrofit.arest

import by.zenkevich_churun.findcell.core.api.arest.ArestsApi
import by.zenkevich_churun.findcell.entity.response.CreateOrUpdateArestResponse
import by.zenkevich_churun.findcell.entity.entity.LightArest
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitApisUtil
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitHolder
import by.zenkevich_churun.findcell.serial.arest.abstr.ArestsDeserializer
import by.zenkevich_churun.findcell.serial.util.protocol.Base64Util
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

        TODO()
    }

    override fun get(
        prisonerId: Int,
        passwordHash: ByteArray
    ): List<LightArest> {

        val passwordBase64 = Base64Util.encode(passwordHash)

        val service = retrofit.create(ArestsService::class.java)
        val response = service
            .getArests(1, prisonerId, passwordBase64)
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
        id: Int ) {
        TODO("Not yet implemented")
    }


    private val retrofit
        get() = retrofitHolder.retrofit
}