package by.zenkevich_churun.findcell.prisoner.api.ram.sched

import by.zenkevich_churun.findcell.core.api.sched.ScheduleApi
import by.zenkevich_churun.findcell.core.api.common.WrongPasswordException
import by.zenkevich_churun.findcell.core.entity.general.Cell
import by.zenkevich_churun.findcell.core.entity.general.Jail
import by.zenkevich_churun.findcell.core.entity.sched.Schedule
import by.zenkevich_churun.findcell.core.util.std.CollectionUtil
import by.zenkevich_churun.findcell.prisoner.api.ram.common.RamJailsStorage
import by.zenkevich_churun.findcell.prisoner.api.ram.common.RamUserStorage
import by.zenkevich_churun.findcell.prisoner.api.ram.jail.RamCellEntity
import java.util.Calendar
import java.util.Random
import javax.inject.Inject
import javax.inject.Singleton


/** Fake implementation of [ScheduleApi] that stores data in RAM. **/
@Singleton
class RamScheduleApi @Inject constructor(): ScheduleApi {
    private val random = Random()

    private var schedule: Schedule? = null


    override fun get(prisonerId: Int, passwordHash: ByteArray): Schedule {
        simulateNetworkRequest(1200L, 1900L)
        RamUserStorage.validate(prisonerId, passwordHash)

        return synchronized(this) {
            val sched = schedule ?: createSchedule().also { schedule = it }
            cloneSchedule(sched)
        }
    }

    override fun update(prisonerId: Int, passwordHash: ByteArray, schedule: Schedule) {
        simulateNetworkRequest(1400L, 2200L)
        RamUserStorage.validate(prisonerId, passwordHash)

        synchronized(this) {
            this.schedule = cloneSchedule(schedule)
        }
    }


    override fun addCell(
        prisonerId: Int, passwordHash: ByteArray,
        jailId: Int, cellNumber: Short ) {

        startCellNetworkRequest(prisonerId, passwordHash)
        synchronized(this) {
            addCell(jailId, cellNumber)
        }
    }

    override fun deleteCell(
        prisonerId: Int, passwordHash: ByteArray,
        jailId: Int, cellNumber: Short ) {

        startCellNetworkRequest(prisonerId, passwordHash)
        synchronized(this) {
            deleteCell(jailId, cellNumber)
        }
    }

    override fun updateCell(
        prisonerId: Int, passwordHash: ByteArray,
        oldJailId: Int, oldCellNumber: Short,
        newJailId: Int, newCellNumber: Short ) {

        startCellNetworkRequest(prisonerId, passwordHash)
        synchronized(this) {
            deleteCell(oldJailId, oldCellNumber)
            addCell(newJailId, newCellNumber)
        }
    }


    private fun createSchedule(): Schedule {
        val jail1 = RamJailsStorage.jails[0]
        val jail2 = RamJailsStorage.jails[1]
        val jail3 = RamJailsStorage.jails[2]

        val cell1 = RamCellEntity(jail1.id, jail1.name, 789,  4)
        val cell2 = RamCellEntity(jail2.id, jail2.name, 123, 10)
        val cell3 = RamCellEntity(jail3.id, jail3.name,  43,  8)

        val period1 =
            RamSchedulePeriod(
                Calendar.getInstance().apply { set(2020, Calendar.NOVEMBER, 20) },
                Calendar.getInstance().apply { set(2020, Calendar.NOVEMBER, 29) },
                0
            )

        val period2 =
            RamSchedulePeriod(
                Calendar.getInstance().apply { set(2020, Calendar.NOVEMBER, 29) },
                Calendar.getInstance().apply { set(2020, Calendar.DECEMBER, 13) },
                1
            )

        val period3 =
            RamSchedulePeriod(
                Calendar.getInstance().apply { set(2020, Calendar.DECEMBER, 13) },
                Calendar.getInstance().apply { set(2020, Calendar.DECEMBER, 26) },
                2
            )

        return Schedule(
            Calendar.getInstance().apply { set(2020, Calendar.NOVEMBER, 20) },
            Calendar.getInstance().apply { set(2020, Calendar.DECEMBER, 26) },
            mutableListOf(cell1, cell2, cell3),
            mutableListOf(period1, period2, period3)
        )
    }


    private fun startCellNetworkRequest(
        prisonerId: Int,
        passwordHash: ByteArray ) {

        simulateNetworkRequest(700L, 1100L)
        if(!RamUserStorage.validate(prisonerId, passwordHash)) {
            throw WrongPasswordException()
        }
    }

    private fun addCell(jailId: Int, cellNumber: Short) {
        val sched = schedule ?: return
        val jail = jailById(jailId)

        val existingCell = sched.cells.find { cell ->
            cell.jailName == jail.name && cell.number == cellNumber
        }

        if(existingCell == null) {
            val cells = sched.cells as MutableList<Cell>
            cells.add( RamCellEntity(jailId, jail.name, cellNumber, 8) )
        }
    }

    private fun deleteCell(jailId: Int, cellNumber: Short) {
        val sched = schedule ?: return
        val jail = jailById(jailId)
        val cells = sched.cells as MutableList<Cell>

        cells.removeAll { cell ->
            cell.jailName == jail.name && cell.number == cellNumber
        }
    }


    private fun simulateNetworkRequest(minTime: Long, maxTime: Long) {
        val delta = (maxTime - minTime).toInt()
        val time = minTime + random.nextInt(delta)
        try {
            Thread.sleep(time)
        } catch(exc: InterruptedException) {
            // Empty.
        }
    }

    private fun cloneSchedule(orig: Schedule): Schedule {
        return Schedule(
            orig.start.clone() as Calendar,
            orig.end.clone() as Calendar,
            CollectionUtil.copyList(orig.cells),
            CollectionUtil.copyList(orig.periods)
        )
    }

    private fun jailById(id: Int): Jail {
        return RamJailsStorage.jails.find { j ->
            j.id == id
        } ?: throw IllegalArgumentException("Wrong Jail ID $id")
    }
}