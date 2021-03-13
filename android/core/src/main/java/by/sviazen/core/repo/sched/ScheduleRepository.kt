package by.sviazen.core.repo.sched

import by.sviazen.domain.entity.Schedule


interface ScheduleRepository {

    fun getSchedule(arestId: Int): GetScheduleResult

    fun updateSchedule(schedule: Schedule): UpdateScheduleResult


    /** @return whether network request was successful. **/
    fun addCell(
        jailId: Int,
        cellNumber: Short
    ): Boolean

    /** @return whether network request was successful. **/
    fun deleteCell(
        jailId: Int,
        cellNumber: Short
    ): Boolean

    /** @return whether network request was successful. **/
    fun updateCell(
        oldJailId: Int, oldCellNumber: Short,
        newJailId: Int, newCellNumber: Short
    ): Boolean
}