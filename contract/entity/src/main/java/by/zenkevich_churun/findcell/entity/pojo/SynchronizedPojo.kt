package by.zenkevich_churun.findcell.entity.pojo

import com.google.gson.annotations.SerializedName


/** A large POJO containing all synced data. **/
class SynchronizedPojo {

    @SerializedName("jails")
    var jails: List<FullJailPojo> = listOf()
}