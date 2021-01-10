package by.zenkevich_churun.findcell.remote.retrofit.cp

import by.zenkevich_churun.findcell.core.api.cp.CoPrisonersApi
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitApisUtil
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitHolder
import javax.inject.Inject


class RetrofitCoPrisonersApi @Inject constructor(
    private val retrofitHolder: RetrofitHolder
): CoPrisonersApi {

    override fun connect(
        prisonerId: Int,
        passwordHash: ByteArray,
        coPrisonerId: Int
    ): CoPrisoner.Relation {

        val response = createService()
            .connect(1, prisonerId, passwordHash, coPrisonerId)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())

        val ordinal = response.body()!!.string().toInt()
        return CoPrisoner.Relation.values()[ordinal]
    }


    private fun createService(): CoPrisonersService {
        return retrofitHolder
            .retrofit
            .create(CoPrisonersService::class.java)
    }
}