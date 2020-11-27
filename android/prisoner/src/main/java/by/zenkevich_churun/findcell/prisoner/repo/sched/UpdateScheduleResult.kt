package by.zenkevich_churun.findcell.prisoner.repo.sched


sealed class UpdateScheduleResult {
    class Failed(
        val exc: Exception
    ): UpdateScheduleResult()

    object Success: UpdateScheduleResult()
    object NoInternet: UpdateScheduleResult()
}