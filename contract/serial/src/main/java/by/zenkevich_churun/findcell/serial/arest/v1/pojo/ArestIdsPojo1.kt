package by.zenkevich_churun.findcell.serial.arest.v1.pojo

import com.google.gson.annotations.SerializedName


class ArestIdsPojo1 {

    @SerializedName("prisoner")
    var prisonerId: Int = 0

    @SerializedName("pass")
    lateinit var passwordHash: ByteArray

    @SerializedName("ids")
    lateinit var arestIds: List<Int>
}