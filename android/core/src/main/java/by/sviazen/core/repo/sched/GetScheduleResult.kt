package by.sviazen.core.repo.sched

import by.sviazen.domain.entity.Schedule
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