package by.zenkevich_churun.findcell.remote.retrofit.jail

import by.zenkevich_churun.findcell.core.api.jail.JailsApi
import by.zenkevich_churun.findcell.entity.entity.Cell
import by.zenkevich_churun.findcell.entity.entity.Jail
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetrofitJailsApi @Inject constructor(): JailsApi {

    override fun jailsList(): List<Jail> {
        TODO("Not yet implemented")
    }

    override fun cell(jailId: Int, cellNumber: Short): Cell {
        TODO("Not yet implemented")
    }
}