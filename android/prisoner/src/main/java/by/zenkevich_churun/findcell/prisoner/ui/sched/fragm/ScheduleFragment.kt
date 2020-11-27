package by.zenkevich_churun.findcell.prisoner.ui.sched.fragm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.zenkevich_churun.findcell.core.entity.sched.Schedule
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.core.util.recycler.autogrid.AutomaticGridLayoutManager
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.CellModel
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.ScheduleModel
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.SchedulePeriodModel
import by.zenkevich_churun.findcell.prisoner.ui.sched.vm.ScheduleViewModel
import kotlinx.android.synthetic.main.schedule_fragm.*
import java.util.Calendar


/** Allows viewing interactive editing of the user's arest [Schedule]. **/
class ScheduleFragment: Fragment(R.layout.schedule_fragm) {

    private lateinit var vm: ScheduleViewModel


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

        val cell3 = CellModel(
            "Барановичи", 43,
            0xff808080.toInt(),
            0xff404040.toInt(),
            0xfff0f0f0.toInt()
        )

        val period1 = SchedulePeriodModel(
            Calendar.getInstance().apply { set(2020, Calendar.NOVEMBER, 20) },
            Calendar.getInstance().apply { set(2020, Calendar.NOVEMBER, 29) },
            0
        )

        val period2 = SchedulePeriodModel(
            Calendar.getInstance().apply { set(2020, Calendar.NOVEMBER, 29) },
            Calendar.getInstance().apply { set(2020, Calendar.DECEMBER, 13) },
            1
        )

        val period3 = SchedulePeriodModel(
            Calendar.getInstance().apply { set(2020, Calendar.DECEMBER, 13) },
            Calendar.getInstance().apply { set(2020, Calendar.DECEMBER, 26) },
            2
        )

        val schedule = Schedule(
            Calendar.getInstance().apply { set(2020, Calendar.NOVEMBER, 20) },
            Calendar.getInstance().apply { set(2020, Calendar.DECEMBER, 26) },
            listOf(cell1, cell2, cell3),
            listOf(period1, period2, period3)
        )

        val scheduleModel = ScheduleModel.fromSchedule(schedule)


        initFields()
        initCellsAdapter()
        initDaysAdapter()
        displaySchedule(scheduleModel)

        vm.selectedCellIndexLD.observe(viewLifecycleOwner, Observer { cellIndex ->
            selectCell(cellIndex)
        })

        view.setOnClickListener {
            vm.unselectCell()
        }
    }


    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = ScheduleViewModel.get(appContext, this)
    }

    private fun initCellsAdapter() {
        recvCells.layoutManager = AutomaticGridLayoutManager(
            requireActivity(),
            dimen(R.dimen.cellview_width)
        )
    }

    private fun initDaysAdapter() {
        val activitySize = AndroidUtil.activitySize(requireActivity())
        recvDays.pivotX = 0.5f*activitySize.width
        recvDays.pivotY = 0.5f*activitySize.height

        val layoutMan = LinearLayoutManager(requireContext())
        val itemDecoration = DividerItemDecoration(requireContext(), layoutMan.orientation)
        recvDays.apply {
            layoutManager = layoutMan
            addItemDecoration(itemDecoration)
        }
    }

    private fun displaySchedule(scheduleModel: ScheduleModel) {
        recvCells.adapter = CellsAdapter(scheduleModel.cells, vm)
        recvDays.adapter = ScheduleDaysAdapter(scheduleModel)
    }

    private fun selectCell(index: Int) {
        val adapter = recvCells.adapter as CellsAdapter
        adapter.selectCellAt(index)
        recvDays.scaleX = if(index < 0) 1.0f else 0.9f
        recvDays.scaleY = if(index < 0) 1.0f else 0.9f
    }


    private fun dimen(dimenRes: Int): Int
        = resources.getDimensionPixelSize(dimenRes)
}