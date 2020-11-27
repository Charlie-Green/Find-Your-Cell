package by.zenkevich_churun.findcell.prisoner.api.ram

import by.zenkevich_churun.findcell.core.entity.sched.SchedulePeriod
import java.util.*


internal class RamSchedulePeriod(
    override val startDate: Calendar,
    override val endDate: Calendar,
    override val cellIndex: Int
): SchedulePeriod()