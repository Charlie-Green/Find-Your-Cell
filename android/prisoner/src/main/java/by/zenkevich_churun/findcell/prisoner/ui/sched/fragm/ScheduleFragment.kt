package by.zenkevich_churun.findcell.prisoner.ui.sched.fragm

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.zenkevich_churun.findcell.core.entity.sched.Schedule
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.core.util.recycler.autogrid.AutomaticGridLayoutManager
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.ScheduleModel
import by.zenkevich_churun.findcell.prisoner.ui.sched.vm.ScheduleViewModel
import kotlinx.android.synthetic.main.schedule_fragm.*


/** Allows viewing interactive editing of the user's arest [Schedule]. **/
class ScheduleFragment: Fragment(R.layout.schedule_fragm) {

    private lateinit var vm: ScheduleViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields()
        initCellsAdapter()
        initDaysAdapter()

        vm.selectedCellIndexLD.observe(viewLifecycleOwner, Observer { cellIndex ->
            selectCell(cellIndex)
        })
        vm.scheduleLD.observe(viewLifecycleOwner, Observer { schedule ->
            displaySchedule(schedule)
        })
        vm.errorLD.observe(viewLifecycleOwner, Observer { message ->
            message?.also { notifyError(it) }
        })
        vm.unsavedChangesLD.observe(viewLifecycleOwner, Observer { thereAreChanges ->
            fabSave.isVisible = thereAreChanges
        })

        view.setOnClickListener {
            vm.unselectCell()
        }
        fabSave.setOnClickListener {
            vm.saveSchedule()
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

    private fun notifyError(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.error_title)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }.setOnDismissListener {
                vm.notifyErrorConsumed()
            }.show()
    }


    private fun dimen(dimenRes: Int): Int
        = resources.getDimensionPixelSize(dimenRes)
}