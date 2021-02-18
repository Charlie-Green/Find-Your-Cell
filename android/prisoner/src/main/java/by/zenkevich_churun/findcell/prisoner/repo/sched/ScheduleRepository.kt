package by.zenkevich_churun.findcell.prisoner.repo.sched

import android.util.Log
import by.zenkevich_churun.findcell.core.api.sched.ScheduleApi
import by.zenkevich_churun.findcell.entity.entity.Schedule
import by.zenkevich_churun.findcell.core.common.prisoner.PrisonerStorage
import by.zenkevich_churun.findcell.core.injected.sync.CoPrisonersCacheManager
import by.zenkevich_churun.findcell.prisoner.repo.sched.result.GetScheduleResult
import by.zenkevich_churun.findcell.prisoner.repo.sched.result.UpdateScheduleResult
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScheduleRepository @Inject constructor(
    private val api: ScheduleApi,
    private val store: PrisonerStorage,
    private val cpMan: CoPrisonersCacheManager ) {

    private var schedule: Schedule? = null


    fun getSchedule(arestId: Int): GetScheduleResult {
        val prisoner = store.prisonerLD.value ?: return GetScheduleResult.NotAuthorized

        return try {
            val schedule = api.get(prisoner.id, prisoner.passwordHash, arestId).also {
                this.schedule = it
            }
            GetScheduleResult.Success(schedule)
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to get schedule: ${exc.javaClass.name}: ${exc.message}")
            GetScheduleResult.Failed(exc)
        }
    }

    fun updateSchedule(schedule: Schedule): UpdateScheduleResult {
        val prisoner = store.prisonerLD.value ?: return UpdateScheduleResult.NotAuthorized

        try {
            api.update(prisoner.id, prisoner.passwordHash, schedule)
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to update schedule: ${exc.javaClass.name}: ${exc.message}")
            return UpdateScheduleResult.Failed(exc)
        }

        // Modification of Schedule may affect suggested CoPrisoners:
        cpMan.invalidate()

        return UpdateScheduleResult.Success
    }


    fun addCell(
        jailId: Int,
        cellNumber: Short

    ) = crudCell { arestId, passwordHash ->
        api.addCell(arestId, passwordHash, jailId, cellNumber)
    }

    fun deleteCell(
        jailId: Int,
        cellNumber: Short

    ) = crudCell { arestId, passwordHash ->
        api.deleteCell(arestId, passwordHash, jailId, cellNumber)
    }

    fun updateCell(
        oldJailId: Int, oldCellNumber: Short,
        newJailId: Int, newCellNumber: Short

    ) = crudCell { arestId, passwordHash ->
        api.updateCell(
            arestId, passwordHash,
            oldJailId, oldCellNumber,
            newJailId, newCellNumber
        )
    }


    private inline fun crudCell(
        performNetworkCall: (arestId: Int, passwordHash: ByteArray) -> Unit
    ): Boolean {

        val sched = schedule ?: return false
        val prisoner = store.prisonerLD.value ?: return false

        try {
            performNetworkCall(sched.arestId, prisoner.passwordHash)
            return true
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to add cell: ${exc.javaClass.name}: ${exc.message}")
            return false
        }
    }


    companion object {
        private const val LOGTAG = "FindCell-Schedule"
    }
}