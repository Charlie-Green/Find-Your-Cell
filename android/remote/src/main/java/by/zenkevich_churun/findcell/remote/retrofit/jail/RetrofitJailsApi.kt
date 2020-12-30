package by.zenkevich_churun.findcell.remote.retrofit.jail

import android.util.Log
import by.zenkevich_churun.findcell.core.api.jail.JailsApi
import by.zenkevich_churun.findcell.entity.entity.Cell
import by.zenkevich_churun.findcell.entity.entity.Jail
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

    override fun cell(jailId: Int, cellNumber: Short): Cell {
        TODO("Not yet implemented")
    }


    private val retrofit
        get() = retrofitHolder.retrofit
}