package by.zenkevich_churun.findcell.remote.retrofit.cp

import by.zenkevich_churun.findcell.core.api.cp.CoPrisonersApi
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.entity.response.GetCoPrisonerResponse
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitApisUtil
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitHolder
import by.zenkevich_churun.findcell.serial.cp.v1.CoPrisonerContactsPojo1
import by.zenkevich_churun.findcell.serial.util.protocol.Base64Util
import by.zenkevich_churun.findcell.serial.util.protocol.ProtocolUtil
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.HttpURLConnection
import javax.inject.Inject


class RetrofitCoPrisonersApi @Inject constructor(
    private val retrofitHolder: RetrofitHolder
): CoPrisonersApi {

    override fun connect(
        prisonerId: Int,
        passwordHash: ByteArray,
        coPrisonerId: Int
    ): CoPrisoner.Relation {

        val passwordBase64 = Base64Util.encode(passwordHash)

        val response = createService()
            .connect(1, prisonerId, passwordBase64, coPrisonerId)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())

        return relationFromResponse(response)
    }

    override fun disconnect(
        prisonerId: Int,
        passwordHash: ByteArray,
        coPrisonerId: Int
    ): CoPrisoner.Relation {

        val passwordBase64 = Base64Util.encode(passwordHash)

        val response = createService()
            .disconnect(1, prisonerId, passwordBase64, coPrisonerId)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())

        return relationFromResponse(response)
    }

    override fun getCoPrisoner(
        prisonerId: Int,
        passwordHash: ByteArray,
        coPrisonerId: Int
    ): GetCoPrisonerResponse {

        val passwordBase64 = Base64Util.encode(passwordHash)

        val response = createService()
            .coPrisoner(1, prisonerId, passwordBase64, coPrisonerId)
            .execute()

        if(response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
            return GetCoPrisonerResponse.NotConnected
        }
        RetrofitApisUtil.assertResponseCode(response.code())

        val istream = response.body()!!.byteStream()
        val pojo = ProtocolUtil
            .fromJson(istream, CoPrisonerContactsPojo1::class.java)

        return GetCoPrisonerResponse.Success(pojo.collectContacts(), pojo.info)
    }


    private fun createService(): CoPrisonersService {
        return retrofitHolder
            .retrofit
            .create(CoPrisonersService::class.java)
    }

    private fun relationFromResponse(
        response: Response<ResponseBody>
    ): CoPrisoner.Relation {
        val ordinal = response.body()!!.string().toInt()
        return CoPrisoner.Relation.values()[ordinal]
    }
}