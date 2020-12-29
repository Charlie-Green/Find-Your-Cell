package by.zenkevich_churun.findcell.server.protocol.serial.arest.v1.pojo

import by.zenkevich_churun.findcell.entity.entity.LightArest
import com.google.gson.annotations.SerializedName
import java.util.Calendar


class ArestPojo1: LightArest() {

    private val calStart by lazy { Calendar.getInstance() }
    private val calEnd   by lazy { Calendar.getInstance() }


    @SerializedName("id")
    override var id: Int = 0

    @SerializedName("start")
    var startMillis: Long = 0L

    @SerializedName("end")
    var endMillis: Long = 0L

    @SerializedName("jails")
    var jailIds: IntArray = intArrayOf()


    override val start: Calendar
        get() = calStart.apply { timeInMillis = startMillis }

    override val end: Calendar
        get() = calEnd.apply { timeInMillis = endMillis }

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