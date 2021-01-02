package by.zenkevich_churun.findcell.serial.arest.v1.pojo

import by.zenkevich_churun.findcell.entity.entity.LightArest
import by.zenkevich_churun.findcell.serial.arest.v1.contract.ArestContract1
import com.google.gson.annotations.SerializedName


internal class ArestsListPojo1(

    @SerializedName(ArestContract1.KEY_ARESTS_LIST)
    var arests: List<ArestPojo1> = listOf() ) {


    companion object {

        fun wrap(arests: List<LightArest>): ArestsListPojo1 {
            val arestPojos = arests.map { a ->
                ArestPojo1.from(a, null, null)
            }
            return ArestsListPojo1(arestPojos)
        }
    }
}