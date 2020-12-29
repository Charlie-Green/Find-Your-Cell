package by.zenkevich_churun.findcell.serial.arest.v1.pojo

import by.zenkevich_churun.findcell.entity.entity.LightArest
import com.google.gson.annotations.SerializedName
import java.util.Calendar


class ArestPojo1: LightArest() {

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

        fun from(a: LightArest): ArestPojo1 {
            if(a is ArestPojo1) {
                return a
            }

            return ArestPojo1().apply {
                id          = a.id
                startMillis = a.start.timeInMillis
                endMillis   = a.end.timeInMillis
                jailIds     = a.jailIds
            }
        }

        private val LightArest.jailIds: IntArray
            get() = IntArray(jailsCount) { index ->
                jailIdAt(index)
            }
    }
}