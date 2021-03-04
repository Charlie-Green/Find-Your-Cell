package by.sviazen.core.api.sched

import by.sviazen.domain.entity.Schedule


/** Performs CRUD operations on user's [Schedule]. **/
interface ScheduleApi {
    fun get(
        arestId: Int,
        passwordHash: ByteArray,
        arestStart: Long,
        arestEnd: Long
    ): Schedule

    fun update(
        passwordHash: ByteArray,
        schedule: Schedule
    )

    fun addCell(
        arestId: Int,
        passwordHash: ByteArray,
        jailId: Int,
        cellNumber: Short
    )

    fun deleteCell(
        arestId: Int,
        passwordHash: ByteArray,
        jailId: Int,
        cellNumber: Short
    )

    fun updateCell(
        arestId: Int, passwordHash: ByteArray,
        oldJailId: Int, oldCellNumber: Short,
        newJailId: Int, newCellNumber: Short
    )
}