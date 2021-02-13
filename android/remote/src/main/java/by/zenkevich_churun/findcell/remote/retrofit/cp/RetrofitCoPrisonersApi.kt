package by.zenkevich_churun.findcell.remote.retrofit.cp

import by.zenkevich_churun.findcell.core.api.cp.CoPrisonersApi
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitApisUtil
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitHolder
import by.zenkevich_churun.findcell.serial.util.protocol.Base64Util
import okhttp3.ResponseBody
import retrofit2.Response
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