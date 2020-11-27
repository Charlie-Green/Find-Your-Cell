package by.zenkevich_churun.findcell.core.api

import by.zenkevich_churun.findcell.core.entity.sched.Schedule


/** Performs CRUD operations on user's [Schedule]. **/
interface ScheduleApi {
    fun get(prisonerId: Int, passwordHash: ByteArray): Schedule
    fun update(prisonerId: Int, passwordHash: ByteArray, schedule: Schedule)
}