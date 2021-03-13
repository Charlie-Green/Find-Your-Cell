package by.sviazen.core.repo.sched


sealed class UpdateScheduleResult {
    class Failed(
        val exc: Exception
    ): UpdateScheduleResult()

    object Success: UpdateScheduleResult()
    object NotAuthorized: UpdateScheduleResult()
}