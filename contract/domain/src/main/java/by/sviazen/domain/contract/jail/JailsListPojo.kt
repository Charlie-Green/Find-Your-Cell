package by.sviazen.domain.contract.jail

import by.sviazen.domain.entity.Jail
import com.google.gson.annotations.SerializedName


/** server -> client to get list of [Jail]s **/
class JailsListPojo(

    @SerializedName("jails")
    var jails: List<JailPojo> ) {

    constructor(): this(listOf())


    companion object {
        fun from(jails: List<Jail>): JailsListPojo {
            val jailPojos = jails.map { j ->
                JailPojo.from(j)
            }
            return JailsListPojo(jailPojos)
        }
    }
}