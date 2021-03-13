package by.sviazen.prisoner.repo.sched

import android.util.Log
import by.sviazen.core.api.sched.ScheduleApi
import by.sviazen.core.common.prisoner.PrisonerStorage
import by.sviazen.core.injected.sync.AutomaticSyncManager
import by.sviazen.core.repo.sched.ScheduleRepository
import by.sviazen.domain.entity.Arest
import by.sviazen.domain.entity.Jail
import by.sviazen.domain.entity.Schedule
import by.sviazen.domain.simpleentity.SimpleJail
import by.sviazen.prisoner.repo.arest.ArestsCache
import by.sviazen.core.repo.sched.GetScheduleResult
import by.sviazen.core.repo.sched.UpdateScheduleResult
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScheduleRepositoryImpl @Inject constructor(
    private val api: ScheduleApi,
    private val store: PrisonerStorage,
    private val arestsCache: ArestsCache,
    private val autoSyncMan: AutomaticSyncManager
): ScheduleRepository {

    private var schedule: Schedule? = null


    override fun getSchedule(arestId: Int): GetScheduleResult {
        val prisoner = store.prisonerLD.value ?: return GetScheduleResult.NotAuthorized
        val arest = arestsCache.getById(arestId) ?: return GetScheduleResult.NotAuthorized

        return try {
            val schedule = api.get(
                arestId,
                prisoner.passwordHash!!, arest.start, arest.end).also {
                this.schedule = it
            }
            GetScheduleResult.Success(schedule)
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to get schedule: ${exc.javaClass.name}: ${exc.message}")
            GetScheduleResult.Failed(exc)
        }
    }

    override fun updateSchedule(schedule: Schedule): UpdateScheduleResult {
        val prisoner = store.prisonerLD.value ?: return UpdateScheduleResult.NotAuthorized

        try {
            api.update(prisoner.passwordHash!!, schedule)
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to update schedule: ${exc.javaClass.name}: ${exc.message}")
            return UpdateScheduleResult.Failed(exc)
        }

        // Modification of Schedule may affect suggested CoPrisoners:
        autoSyncMan.set(true)

        // Update the Arest this Schedule belongs to:
        updateArest(schedule)

        return UpdateScheduleResult.Success
    }


    override fun addCell(
        jailId: Int,
        cellNumber: Short

    ) = crudCell { arestId, passwordHash ->
        api.addCell(arestId, passwordHash, jailId, cellNumber)
    }

    override fun deleteCell(
        jailId: Int,
        cellNumber: Short

    ) = crudCell { arestId, passwordHash ->
        api.deleteCell(arestId, passwordHash, jailId, cellNumber)
    }

    override fun updateCell(
        oldJailId: Int, oldCellNumber: Short,
        newJailId: Int, newCellNumber: Short

    ) = crudCell { arestId, passwordHash ->
        api.updateCell(
            arestId, passwordHash,
            oldJailId, oldCellNumber,
            newJailId, newCellNumber
        )
    }


    private fun updateArest(schedule: Schedule) {
        val oldArest = arestsCache.cachedList.find { a ->
            a.id == schedule.arestId
        } ?: return

        val newJailIds = hashSetOf<Int>()
        val newJails = mutableListOf<Jail>()
        for(period in schedule.periods) {
            val cell = schedule.cells[period.cellIndex]
            val jail = SimpleJail(
                cell.jailId,
                cell.jailName,
                0  // Doesn't matter in this case.
            )

            if(!newJailIds.contains(jail.id)) {
                newJailIds.add(jail.id)
                newJails.add(jail)
            }
        }

        val newArest = Arest(
            oldArest.id, oldArest.start, oldArest.end, newJails)
        arestsCache.update(newArest)
    }

    private inline fun crudCell(
        performNetworkCall: (arestId: Int, passwordHash: ByteArray) -> Unit
    ): Boolean {

        val sched = schedule ?: return false
        val prisoner = store.prisonerLD.value ?: return false

        try {
            performNetworkCall(sched.arestId, prisoner.passwordHash!!)
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