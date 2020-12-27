package by.zenkevich_churun.findcell.remote.lan.sched

import by.zenkevich_churun.findcell.core.api.sched.ScheduleApi
import by.zenkevich_churun.findcell.entity.Schedule


class RetrofitScheduleApi: ScheduleApi {

    override fun get(
        prisonerId: Int,
        passwordHash: ByteArray,
        arestId: Int
    ): Schedule {
        TODO("Not yet implemented")
    }

    override fun update(
        prisonerId: Int,
        passwordHash: ByteArray,
        schedule: Schedule ) {

    }

    override fun addCell(
        prisonerId: Int,
        passwordHash: ByteArray,
        jailId: Int,
        cellNumber: Short ) {

    }

    override fun updateCell(
        prisonerId: Int,
        passwordHash: ByteArray,
        oldJailId: Int,
        oldCellNumber: Short,
        newJailId: Int,
        newCellNumber: Short ) {
        TODO("Not yet implemented")
    }

    override fun deleteCell(
        prisonerId: Int,
        passwordHash: ByteArray,
        jailId: Int,
        cellNumber: Short ) {

    }
}