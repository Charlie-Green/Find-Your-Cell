package by.zenkevich_churun.findcell.remote.retrofit.jail

import by.zenkevich_churun.findcell.core.api.jail.JailsApi
import by.zenkevich_churun.findcell.domain.entity.Cell
import by.zenkevich_churun.findcell.domain.entity.Jail
import by.zenkevich_churun.findcell.domain.simpleentity.SimpleCell
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitApisUtil
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitHolder
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetrofitJailsApi @Inject constructor(
    private val retrofitHolder: RetrofitHolder
): JailsApi {

    override fun jailsList(): List<Jail> {
        val service = retrofit.create(JailsService::class.java)

        val response = service
            .getJails(1)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())

        return response.body()!!.jails
    }

    override fun cells(jailId: Int, jailName: String): List<Cell> {
        val service = retrofit.create(JailsService::class.java)

        val response = service
            .getCells(1, jailId)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())

        val seats = response.body()!!.seatCounts
        return seats.mapIndexed { index, seatCount ->
            SimpleCell(
                jailId,
                jailName,
                (index + 1).toShort(),
                seatCount
            )
        }
    }


    private val retrofit
        get() = retrofitHolder.retrofit
}