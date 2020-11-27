package by.zenkevich_churun.findcell.prisoner.repo.sched

import by.zenkevich_churun.findcell.core.entity.sched.Schedule


sealed class GetScheduleResult {
    class Success(
        val schedule: Schedule
    ): GetScheduleResult()

    class Failed(
        val exc: Exception
    ): GetScheduleResult()

    object NoInternet: GetScheduleResult()
}