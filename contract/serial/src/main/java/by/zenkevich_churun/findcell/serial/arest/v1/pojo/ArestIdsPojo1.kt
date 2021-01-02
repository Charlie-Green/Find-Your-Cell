package by.zenkevich_churun.findcell.serial.arest.v1.pojo

import by.zenkevich_churun.findcell.serial.arest.pojo.ArestIdsPojo
import com.google.gson.annotations.SerializedName


class ArestIdsPojo1: ArestIdsPojo() {

    @SerializedName("prisoner")
    override var prisonerId: Int = 0

    @SerializedName("pass")
    override lateinit var passwordHash: ByteArray

    @SerializedName("ids")
    override lateinit var arestIds: List<Int>
}