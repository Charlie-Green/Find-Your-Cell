package by.zenkevich_churun.findcell.domain.contract.arest

import by.zenkevich_churun.findcell.domain.entity.Arest
import by.zenkevich_churun.findcell.domain.entity.LightArest
import com.google.gson.annotations.SerializedName


/** server -> client to get a user's list of [Arest]s. **/
class ArestsListPojo(

    @SerializedName("arests")
    var arests: List<ArestPojo> ) {


    constructor(): this(listOf())


    companion object {

        fun from(arests: List<LightArest>): ArestsListPojo {
            val pojos = arests.map { arest ->
                ArestPojo.from(arest)
            }
            return ArestsListPojo(pojos)
        }
    }
}