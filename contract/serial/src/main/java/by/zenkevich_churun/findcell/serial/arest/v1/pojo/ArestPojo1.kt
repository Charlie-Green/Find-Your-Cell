package by.zenkevich_churun.findcell.serial.arest.v1.pojo

import by.zenkevich_churun.findcell.entity.entity.LightArest
import by.zenkevich_churun.findcell.serial.arest.pojo.ArestPojo
import by.zenkevich_churun.findcell.serial.util.protocol.Base64Util
import com.google.gson.annotations.SerializedName
import java.util.Calendar


internal class ArestPojo1: ArestPojo() {

    @SerializedName("prisoner")
    override var prisonerId: Int? = null

    @SerializedName("pass")
    override var passwordBase64: String? = null

    @SerializedName("id")
    override var id: Int = 0

    @SerializedName("start")
    var startMillis: Long = 0L

    @SerializedName("end")
    var endMillis: Long = 0L

    @SerializedName("jails")
    var jailIds: IntArray = intArrayOf()


    override val start: Calendar
        get() = Calendar.getInstance().apply { timeInMillis = startMillis }

    override val end: Calendar
        get() = Calendar.getInstance().apply { timeInMillis = endMillis }

    override val jailsCount: Int
        get() = jailIds.size

    override fun jailIdAt(index: Int): Int
        = jailIds[index]


    companion object {

        fun from(
            a: LightArest,
            prisonerId: Int?,
            passwordHash: ByteArray?
        ): ArestPojo1 {

            val passwordBase64 = passwordHash?.let { Base64Util.encode(it) }

            if(a is ArestPojo1) {
                a.prisonerId = prisonerId
                a.passwordBase64 = passwordBase64
                return a
            }

            val pojo = ArestPojo1()
            pojo.prisonerId     = prisonerId
            pojo.passwordBase64 = passwordBase64
            pojo.id             = a.id
            pojo.startMillis    = a.start.timeInMillis
            pojo.endMillis      = a.end.timeInMillis
            pojo.jailIds        = a.jailIds

            return pojo
        }

        private val LightArest.jailIds: IntArray
            get() = IntArray(jailsCount) { index ->
                jailIdAt(index)
            }
    }
}