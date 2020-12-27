package by.zenkevich_churun.findcell.remote.lan.jail

import by.zenkevich_churun.findcell.core.api.jail.JailsApi
import by.zenkevich_churun.findcell.entity.Cell
import by.zenkevich_churun.findcell.entity.Jail


class RetrofitJailsApi: JailsApi {

    override fun jailsList(): List<Jail> {
        TODO("Not yet implemented")
    }

    override fun cell(jailId: Int, cellNumber: Short): Cell {
        TODO("Not yet implemented")
    }
}