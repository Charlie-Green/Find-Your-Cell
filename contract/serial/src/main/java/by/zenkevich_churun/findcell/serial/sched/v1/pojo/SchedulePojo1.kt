package by.zenkevich_churun.findcell.serial.sched.v1.pojo

import by.zenkevich_churun.findcell.entity.entity.*
import by.zenkevich_churun.findcell.serial.sched.pojo.SchedulePojo
import com.google.gson.annotations.SerializedName
import java.util.Calendar


class SchedulePojo1: SchedulePojo() {

    @SerializedName("id")
    var nullableArestId: Int? = null

    @SerializedName("start")
    var startMillis: Long = 0L

    @SerializedName("end")
    var endMillis: Long = 0L

    @SerializedName("pass")
    override var passwordBase64: String? = null

    @SerializedName("cells")
    override var cells: List<Cell> = listOf()

    @SerializedName("periods")
    override var periods: List<SchedulePeriod> = listOf()


    override val arestId: Int
        get() = nullableArestId ?: Arest.INVALID_ID

    override val start: Calendar
        get() = Calendar.getInstance().apply { timeInMillis = startMillis }

    override val end: Calendar
        get() = Calendar.getInstance().apply { timeInMillis = endMillis }
}