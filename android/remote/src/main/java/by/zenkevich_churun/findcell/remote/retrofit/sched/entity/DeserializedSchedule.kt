package by.zenkevich_churun.findcell.remote.retrofit.sched.entity

import by.zenkevich_churun.findcell.entity.entity.Cell
import by.zenkevich_churun.findcell.entity.entity.Schedule
import by.zenkevich_churun.findcell.entity.entity.SchedulePeriod
import java.util.*


internal class DeserializedSchedule: Schedule() {

    override val arestId: Int
        get() = TODO("Not yet implemented")

    override val start: Calendar
        get() = TODO("Not yet implemented")

    override val end: Calendar
        get() = TODO("Not yet implemented")

    override val cells: List<Cell>
        get() = TODO("Not yet implemented")

    override val periods: List<SchedulePeriod>
        get() = TODO("Not yet implemented")
}