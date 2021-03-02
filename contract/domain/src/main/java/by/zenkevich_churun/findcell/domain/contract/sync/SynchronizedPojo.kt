package by.zenkevich_churun.findcell.domain.contract.sync

import by.zenkevich_churun.findcell.domain.contract.cp.CoPrisonerHeaderPojo
import by.zenkevich_churun.findcell.domain.contract.jail.FullJailPojo
import com.google.gson.annotations.SerializedName


/** server -> client during sync. **/
class SynchronizedPojo(

    @SerializedName("cps")
    var coPrisoners: List<CoPrisonerHeaderPojo>,

    @SerializedName("jails")
    var jails: List<FullJailPojo>
)