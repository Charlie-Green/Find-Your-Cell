package by.zenkevich_churun.findcell.serial.sync.v1

import com.google.gson.annotations.SerializedName


/** A large POJO containing all synced data. **/
class SynchronizedPojo1 {

    @SerializedName("jails")
    var jails: List<FullJailPojo1> = listOf()

    @SerializedName("prisoners")
    var coPrisoners: List<CoPrisonerPojo1> = listOf()
}