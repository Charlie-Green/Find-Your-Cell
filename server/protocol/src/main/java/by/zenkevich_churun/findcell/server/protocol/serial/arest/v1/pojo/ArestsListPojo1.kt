package by.zenkevich_churun.findcell.server.protocol.serial.arest.v1.pojo

import by.zenkevich_churun.findcell.serial.arest.v1.ArestContract1
import com.google.gson.annotations.SerializedName


class ArestsListPojo1 {

    @SerializedName(ArestContract1.KEY_ARESTS_LIST)
    lateinit var arests: List<ArestPojo1>
}