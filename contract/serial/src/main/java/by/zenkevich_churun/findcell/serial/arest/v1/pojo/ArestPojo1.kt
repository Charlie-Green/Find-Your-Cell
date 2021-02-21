package by.zenkevich_churun.findcell.serial.arest.v1.pojo

import by.zenkevich_churun.findcell.entity.entity.LightArest
import by.zenkevich_churun.findcell.serial.arest.pojo.ArestPojo
import com.google.gson.annotations.SerializedName


class ArestPojo1: ArestPojo() {

    @SerializedName("prisoner")
    override var prisonerId: Int? = null

    @SerializedName("pass")
    override var passwordBase64: String? = null

    @SerializedName("id")
    override var id: Int = 0

    @SerializedName("start")
    override var start: Long = 0L

    @SerializedName("end")
    override var end: Long = 0L

    @SerializedName("jails")
    var jailIds: IntArray = intArrayOf()


    override val jailsCount: Int
        get() = jailIds.size

    override fun jailIdAt(index: Int): Int
        = jailIds[index]


    companion object {

        fun from(
            a: LightArest,
            prisonerId: Int?,
            passwordBase64: String?
        ): ArestPojo1 {

            if(a is ArestPojo1) {
                a.prisonerId = prisonerId
                a.passwordBase64 = passwordBase64
                return a
            }

            val pojo = ArestPojo1()
            pojo.prisonerId     = prisonerId
            pojo.passwordBase64 = passwordBase64
            pojo.id             = a.id
            pojo.start          = a.start
            pojo.end            = a.end
            pojo.jailIds        = a.jailIds

            return pojo
        }

        private val LightArest.jailIds: IntArray
            get() = IntArray(jailsCount) { index ->
                jailIdAt(index)
            }
    }
}