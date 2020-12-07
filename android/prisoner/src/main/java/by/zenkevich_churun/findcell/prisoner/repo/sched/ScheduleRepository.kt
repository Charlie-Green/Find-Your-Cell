package by.zenkevich_churun.findcell.prisoner.repo.sched

import android.util.Log
import by.zenkevich_churun.findcell.core.api.ScheduleApi
import by.zenkevich_churun.findcell.core.entity.sched.Schedule
import by.zenkevich_churun.findcell.prisoner.repo.common.PrisonerStorage
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScheduleRepository @Inject constructor(
    private val api: ScheduleApi,
    private val store: PrisonerStorage ) {

    fun getSchedule(): GetScheduleResult {
        val prisoner = store.prisonerLD.value ?: return GetScheduleResult.NotAuthorized

        return try {
            val schedule = api.get(prisoner.id, prisoner.passwordHash)
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
            return UpdateScheduleResult.Success
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to update schedule: ${exc.javaClass.name}: ${exc.message}")
            return UpdateScheduleResult.Failed(exc)
        }
    }


    fun addCell(
        jailId: Int,
        cellNumber: Short
    ): Boolean {

        val prisoner = store.prisonerLD.value ?: return false

        try {
            api.addCell(prisoner.id, prisoner.passwordHash, jailId, cellNumber)
            return true
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to add cell: ${exc.javaClass.name}: ${exc.message}")
            return false
        }
    }

    fun deleteCell(
        jailId: Int,
        cellNumber: Short
    ): Boolean {

        val prisoner = store.prisonerLD.value ?: return false

        try {
            api.deleteCell(prisoner.id, prisoner.passwordHash, jailId, cellNumber)
            return true
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to delete cell: ${exc.javaClass.name}: ${exc.message}")
            return false
        }
    }

    fun updateCell(
        oldJailId: Int, oldCellNumber: Short,
        newJailId: Int, newCellNumber: Short
    ): Boolean {

        val prisoner = store.prisonerLD.value ?: return false
        Log.v("CharlieDebug", "Update: ($oldJailId; $oldCellNumber) -> ($newJailId; $newCellNumber)")

        try {
            api.updateCell(
                prisoner.id, prisoner.passwordHash,
                oldJailId, oldCellNumber,
                newJailId, newCellNumber
            )
            return true
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to update cell: ${exc.javaClass.name}: ${exc.message}")
            return false
        }
    }


    companion object {
        private const val LOGTAG = "FindCell-Schedule"
    }
}