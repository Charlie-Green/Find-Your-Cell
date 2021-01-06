package by.zenkevich_churun.findcell.core.api.sched

import by.zenkevich_churun.findcell.entity.entity.Schedule


/** Performs CRUD operations on user's [Schedule]. **/
interface ScheduleApi {
    fun get(
        prisonerId: Int,
        passwordHash: ByteArray,
        arestId: Int
    ): Schedule

    fun update(
        prisonerId: Int,
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