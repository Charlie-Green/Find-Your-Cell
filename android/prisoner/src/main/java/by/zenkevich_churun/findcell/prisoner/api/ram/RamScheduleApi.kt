package by.zenkevich_churun.findcell.prisoner.api.ram

import by.zenkevich_churun.findcell.core.api.ScheduleApi
import by.zenkevich_churun.findcell.core.entity.sched.Schedule
import by.zenkevich_churun.findcell.core.util.std.CollectionUtil
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


    private fun createSchedule(): Schedule {
        val cell1 = RamCell("Окрестина ЦИП", 789, 4)
        val cell2 = RamCell("Жодино", 123, 10)
        val cell3 = RamCell("Барановичи", 43, 8)

        val period1 = RamSchedulePeriod(
            Calendar.getInstance().apply { set(2020, Calendar.NOVEMBER, 20) },
            Calendar.getInstance().apply { set(2020, Calendar.NOVEMBER, 29) },
            0
        )

        val period2 = RamSchedulePeriod(
            Calendar.getInstance().apply { set(2020, Calendar.NOVEMBER, 29) },
            Calendar.getInstance().apply { set(2020, Calendar.DECEMBER, 13) },
            1
        )

        val period3 = RamSchedulePeriod(
            Calendar.getInstance().apply { set(2020, Calendar.DECEMBER, 13) },
            Calendar.getInstance().apply { set(2020, Calendar.DECEMBER, 26) },
            2
        )

        return Schedule(
            Calendar.getInstance().apply { set(2020, Calendar.NOVEMBER, 20) },
            Calendar.getInstance().apply { set(2020, Calendar.DECEMBER, 26) },
            listOf(cell1, cell2, cell3),
            listOf(period1, period2, period3)
        )
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
}