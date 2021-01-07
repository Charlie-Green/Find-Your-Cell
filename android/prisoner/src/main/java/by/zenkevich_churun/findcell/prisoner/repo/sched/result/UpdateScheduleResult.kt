package by.zenkevich_churun.findcell.prisoner.repo.sched.result


sealed class UpdateScheduleResult {
    class Failed(
        val exc: Exception
    ): UpdateScheduleResult()

    object Success: UpdateScheduleResult()
    object NotAuthorized: UpdateScheduleResult()
}