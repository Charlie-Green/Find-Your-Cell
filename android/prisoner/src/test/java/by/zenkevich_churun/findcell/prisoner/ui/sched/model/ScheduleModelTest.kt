package by.zenkevich_churun.findcell.prisoner.ui.sched.model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*


class ScheduleModelTest {

    @Test
    fun beginEarlier() {
        val schedule = simpleSchedule()
        schedule.beginPeriodEarlier(1)
        assertPeriods(schedule, period(1, 1), period(1, 3))
    }

    @Test
    fun beginLater() {
        val schedule = simpleSchedule()
        schedule.beginPeriodLater(1)
        assertPeriods(schedule, period(1, 3), period(3, 3))
    }

    @Test
    fun finishEarlier() {
        val schedule = simpleSchedule()
        schedule.finishPeriodEarlier(0)
        assertPeriods(schedule, period(1, 1), period(1, 3))
    }

    @Test
    fun finishLater() {
        val schedule = simpleSchedule()
        schedule.finishPeriodLater(0)
        assertPeriods(schedule, period(1, 3), period(3, 3))
    }

    @Test
    fun addPeriod() {
        val schedule = averageSchedule()
        schedule.addPeriod(0, day(3))
        assertPeriods(schedule, period(1, 3), period(3, 3), period(5, 6))
    }

    @Test
    fun addPeriodSplit() {
        val schedule = averageSchedule()
        schedule.addPeriod(0, day(2))
        assertPeriods(schedule, period(1, 2), period(2, 2), period(2, 3), period(5, 6))
    }


    private fun day(date: Int): Calendar {
        return Calendar.getInstance().apply {
            set(2020, Calendar.OCTOBER, date, 0, 0, 0)
        }
    }

    private fun period(startDate: Int, endDate: Int): SchedulePeriodModel {
        return SchedulePeriodModel(day(startDate), day(endDate), 0, 0)
    }

    private fun daysEqual(d1: Calendar, d2: Calendar): Boolean {
        return dayIs(d1, d2[Calendar.DATE])
    }

    private fun dayIs(what: Calendar, desiredDate: Int): Boolean {
        return what[Calendar.DATE] == desiredDate
    }


    private fun assertPeriods(
        schedule: ScheduleModel,
        vararg periods: SchedulePeriodModel ) {

        Assertions.assertEquals(periods.size, schedule.periods.size)
        for(j in periods.indices) {
            Assertions.assertTrue( daysEqual(schedule.periods[j].startDate, periods[j].startDate) )
            Assertions.assertTrue( daysEqual(schedule.periods[j].endDate,   periods[j].endDate) )
        }
    }


    private fun simpleSchedule(): ScheduleModel {
        return ScheduleModel(
            day(1),
            day(3),
            mutableListOf(),
            mutableListOf(
                period(1, 2),
                period(2, 3)
            ),
            mutableListOf()
        )
    }

    private fun averageSchedule(): ScheduleModel {
        return ScheduleModel(
            day(1),
            day(6),
            mutableListOf(
                CellModel("", 0, 0),
                CellModel("", 0, 0),
                CellModel("", 0, 0)
            ),
            mutableListOf(
                period(1, 3),
                period(5, 6)
            ),
            mutableListOf()
        )
    }
}