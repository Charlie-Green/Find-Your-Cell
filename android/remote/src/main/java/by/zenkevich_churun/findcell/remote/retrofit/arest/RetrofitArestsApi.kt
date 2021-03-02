package by.zenkevich_churun.findcell.remote.retrofit.arest

import by.zenkevich_churun.findcell.core.api.arest.ArestsApi
import by.zenkevich_churun.findcell.domain.contract.arest.AddedArestPojo
import by.zenkevich_churun.findcell.domain.contract.arest.ArestsListPojo
import by.zenkevich_churun.findcell.domain.contract.arest.DeletedArestsPojo
import by.zenkevich_churun.findcell.domain.entity.LightArest
import by.zenkevich_churun.findcell.domain.response.CreateOrUpdateArestResponse
import by.zenkevich_churun.findcell.domain.util.Base64Coder
import by.zenkevich_churun.findcell.domain.util.Deserializer
import by.zenkevich_churun.findcell.domain.util.Serializer
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitApisUtil
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitHolder
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetrofitArestsApi @Inject constructor(
    private val retrofitHolder: RetrofitHolder,
    private val base64: Base64Coder
): ArestsApi {

    override fun create(
        prisonerId: Int,
        passwordHash: ByteArray,
        start: Long,
        end: Long
    ): CreateOrUpdateArestResponse {

        val service = retrofit.create(ArestsService::class.java)

        val passwordBase64 = base64.encode(passwordHash)
        val arest = AddedArestPojo(prisonerId, passwordBase64, start, end)

        val approxSize = arest.passwordBase64.length + 60
        val json = Serializer.toJsonString(arest, approxSize)
        val mediaType = MediaType.get("application/json")
        val requestBody = RequestBody.create(mediaType, json)

        val response = service
            .create(requestBody)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())

        return Deserializer.fromJsonStream(
            response.body()!!.byteStream(),
            CreateOrUpdateArestResponse::class.java
        )
    }

    override fun get(
        prisonerId: Int,
        passwordHash: ByteArray
    ): List<LightArest> {

        val passwordBase64 = base64.encode(passwordHash)

        val service = retrofit.create(ArestsService::class.java)
        val response = service
            .get(1, prisonerId, passwordBase64)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())

        val istream = response.body()!!.byteStream()
        return Deserializer
            .fromJsonStream(istream, ArestsListPojo::class.java)
            .arests
    }

    override fun update(
        prisonerId: Int,
        passwordHash: ByteArray,
        id: Int,
        newStart: Long,
        newEnd: Long
    ): CreateOrUpdateArestResponse {

        TODO("Not implemented in Sviazeń 1.0")
    }

    override fun delete(
        prisonerId: Int,
        passwordHash: ByteArray,
        ids: Collection<Int> ) {

        val service = retrofit.create(ArestsService::class.java)

        val pojo = DeletedArestsPojo(
            prisonerId,
            base64.encode(passwordHash),
            if(ids is List) ids else ids.toList()
        )

        val approxSize = pojo.passwordBase64.length + 10*pojo.arestIds.size + 32
        val json = Serializer.toJsonString(pojo, approxSize)
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