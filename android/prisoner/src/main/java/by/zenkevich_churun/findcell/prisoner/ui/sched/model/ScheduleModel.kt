package by.zenkevich_churun.findcell.prisoner.ui.sched.model

import java.util.*


class ScheduleModel(
    val start: Calendar,
    val end: Calendar,
    val cells: MutableList<CellModel>,
    val periods: MutableList<SchedulePeriodModel>,
    val days: MutableList<ScheduleDayModel> ) {

    fun beginPeriodEarlier(periodIndex: Int) {
        val reducePreviousPeriod = periodBordersWithPrevious(periodIndex)

        periods[periodIndex].beginEarlier()
        if(reducePreviousPeriod) {
            val reducedPeriod = periods[periodIndex-1].finishEarlier()
            if(reducedPeriod == null) {
                periods.removeAt(periodIndex-1)
            }
        }
    }

    fun beginPeriodLater(periodIndex: Int) {
        val expandPreviousPeriod = periodBordersWithPrevious(periodIndex)
        val reducedPeriod = periods[periodIndex].beginLater()

        if(expandPreviousPeriod) {
            periods[periodIndex-1].finishLater()
        }

        if(reducedPeriod == null) {
            periods.removeAt(periodIndex)
        }
    }

    fun finishPeriodEarlier(periodIndex: Int) {
        val expandNextPeriod = periodBordersWithNext(periodIndex)
        val reducedPeriod = periods[periodIndex].finishEarlier()

        if(expandNextPeriod) {
            periods[periodIndex+1].beginEarlier()
        }

        if(reducedPeriod == null) {
            periods.removeAt(periodIndex)
        }
    }

    fun finishPeriodLater(periodIndex: Int) {
        val reduceNextPeriod = periodBordersWithNext(periodIndex)

        periods[periodIndex].finishLater()
        if(reduceNextPeriod) {
            val reducedPeriod = periods[periodIndex+1].beginLater()
            if(reducedPeriod == null) {
                periods.removeAt(periodIndex+1)
            }
        }
    }


    fun addPeriod(cellIndex: Int, day: Calendar) {
        ScheduleModelUtil.setToMidnight(day)
        val newPeriod = cells[cellIndex].createPeriod(day, cellIndex)
        val lastPeriodIndex = findPeriodBefore(day)

        if(lastPeriodIndex in periods.indices) {
            val lastPeriod = periods[lastPeriodIndex]

            if(lastPeriod.startDate.before(day) &&
                lastPeriod.endDate.after(day) ) {

                val splitPeriods = lastPeriod.split(day)
                periods[lastPeriodIndex] = splitPeriods.first
                periods.addAll(
                    lastPeriodIndex + 1,
                    listOf(newPeriod, splitPeriods.second)
                )
            }
        } else {
            periods.add(lastPeriodIndex + 1, newPeriod)
        }
    }


    private fun periodBordersWithPrevious(periodIndex: Int): Boolean {
        if(periodIndex == 0) {
            return false
        }
        return periods[periodIndex-1].endDate == periods[periodIndex].startDate
    }

    private fun periodBordersWithNext(periodIndex: Int): Boolean {
        if(periodIndex == periods.lastIndex) {
            return false
        }
        return periods[periodIndex].endDate == periods[periodIndex+1].startDate
    }

    private fun findPeriodBefore(day: Calendar): Int {
        var index = -1

        for(j in periods.indices) {
            if(periods[j].startDate.after(day)) {
                break
            }
            index = j
        }

        return index
    }
}