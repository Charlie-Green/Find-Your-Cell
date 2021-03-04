package by.sviazen.domain.contract.sync

import by.sviazen.domain.contract.cp.CoPrisonerHeaderPojo
import by.sviazen.domain.contract.jail.FullJailPojo
import com.google.gson.annotations.SerializedName


/** server -> client during sync. **/
class SynchronizedPojo(

    @SerializedName("cps")
    var coPrisoners: List<CoPrisonerHeaderPojo>,

    @SerializedName("jails")
    var jails: List<FullJailPojo>
)