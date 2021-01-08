package by.zenkevich_churun.findcell.serial.sync.v1

import by.zenkevich_churun.findcell.entity.pojo.SynchronizedPojo
import com.google.gson.annotations.SerializedName


/** A large POJO containing all synced data. **/
class SynchronizedPojo1: SynchronizedPojo() {

    @SerializedName("jails")
    override var jails: List<FullJailPojo1> = listOf()

    @SerializedName("prisoners")
    override var coPrisoners: List<CoPrisonerPojo1> = listOf()
}