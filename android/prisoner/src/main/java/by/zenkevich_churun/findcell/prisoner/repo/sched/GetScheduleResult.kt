package by.zenkevich_churun.findcell.prisoner.repo.sched

import by.zenkevich_churun.findcell.entity.entity.Schedule
import java.io.IOException


sealed class GetScheduleResult {
    class Success(
        val schedule: Schedule
    ): GetScheduleResult()

    class Failed(
        val exc: IOException
    ): GetScheduleResult()

    object NotAuthorized: GetScheduleResult()
}