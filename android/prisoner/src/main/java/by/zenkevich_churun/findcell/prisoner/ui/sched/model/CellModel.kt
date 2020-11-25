package by.zenkevich_churun.findcell.prisoner.ui.sched.model

import by.zenkevich_churun.findcell.core.entity.general.Cell
import java.util.*


class CellModel(
    override val jailName: String,
    override val number: Short,
    val color: Int
): Cell() {

    override val seats: Short
        get() = throw NotImplementedError("Number of seats doesn't matter.")

    override fun toString(): String
        = "$jailName, $number"

    fun createPeriod(day: Calendar, cellIndex: Int): SchedulePeriodModel {
        return SchedulePeriodModel(
            day,
            day.clone() as Calendar,
            cellIndex,
            color
        )
    }
}