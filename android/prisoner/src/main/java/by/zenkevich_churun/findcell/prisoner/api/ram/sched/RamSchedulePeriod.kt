package by.zenkevich_churun.findcell.prisoner.api.ram.sched

import by.zenkevich_churun.findcell.core.entity.sched.SchedulePeriod
import java.util.*


class RamSchedulePeriod(
    override val startDate: Calendar,
    override val endDate: Calendar,
    override val cellIndex: Int
): SchedulePeriod()