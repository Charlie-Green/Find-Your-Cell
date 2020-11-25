package by.zenkevich_churun.findcell.prisoner.ui.sched.fragm

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.zenkevich_churun.findcell.core.entity.sched.Schedule
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.CellModel
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.ScheduleModel
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.SchedulePeriodModel
import by.zenkevich_churun.findcell.prisoner.util.view.period.SchedulePeriodResizedListener
import kotlinx.android.synthetic.main.schedule_fragm.*
import java.util.*


/** Allows viewing interactive editing of the user's arest [Schedule]. **/
class ScheduleFragment: Fragment(R.layout.schedule_fragm) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cell1 = CellModel(
            "Окрестина ЦИП", 789,
            0xfff08000.toInt(),
            0xff004000.toInt(),
            0xff000000.toInt()
        )

        val cell2 = CellModel(
            "Жодино", 123,
            0xfff000f0.toInt(),
            0xff0000f0.toInt(),
            0xff00f000.toInt()
        )

        val period1 = SchedulePeriodModel(
            Calendar.getInstance().apply { set(2020, Calendar.NOVEMBER, 20) },
            Calendar.getInstance().apply { set(2020, Calendar.NOVEMBER, 22) },
            0
        )

        val period2 = SchedulePeriodModel(
            Calendar.getInstance().apply { set(2020, Calendar.NOVEMBER, 22) },
            Calendar.getInstance().apply { set(2020, Calendar.NOVEMBER, 23) },
            1
        )

        val schedule = ScheduleModel(
            Calendar.getInstance().apply { set(2020, Calendar.NOVEMBER, 20) },
            Calendar.getInstance().apply { set(2020, Calendar.NOVEMBER, 23) },
            mutableListOf(cell1, cell2),
            mutableListOf(period1, period2),
            mutableListOf()
        )

        recvPeriods.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = SchedulePeriodsAdapter(schedule)
        }
    }

//    private fun showCell(cell: CellModel) {
//        cellView.apply {
//            setBackgroundColor(cell.backColor)
//            setNumberBackgroundColor(cell.numberBackColor)
//            setTextColor(cell.textColor)
//            show(cell)
//        }
//    }
}