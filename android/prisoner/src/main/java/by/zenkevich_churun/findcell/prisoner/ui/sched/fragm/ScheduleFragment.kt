package by.zenkevich_churun.findcell.prisoner.ui.sched.fragm

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.zenkevich_churun.findcell.core.entity.general.Cell
import by.zenkevich_churun.findcell.core.entity.sched.Schedule
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.core.util.recycler.autogrid.AutomaticGridLayoutManager
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.celledit.dialog.CellEditorDialog
import by.zenkevich_churun.findcell.prisoner.ui.cellopt.dialog.CellOptionsDialog
import by.zenkevich_churun.findcell.prisoner.ui.common.model.CellUpdate
import by.zenkevich_churun.findcell.prisoner.ui.common.model.ScheduleModel
import by.zenkevich_churun.findcell.prisoner.ui.sched.vm.ScheduleViewModel
import kotlinx.android.synthetic.main.schedule_fragm.*


/** Allows viewing interactive editing of the user's arest [Schedule]. **/
class ScheduleFragment: Fragment(R.layout.schedule_fragm) {

    private lateinit var vm: ScheduleViewModel
    private var selectedCellIndex = -1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields()
        initCellsAdapter()
        initDaysAdapter()

        vm.selectedCellIndexLD.observe(viewLifecycleOwner, Observer { cellIndex ->
            selectedCellIndex = cellIndex
            selectCell()
        })
        vm.scheduleLD.observe(viewLifecycleOwner, Observer { schedule ->
            displaySchedule(schedule)
            selectCell()
            buAddCell.isEnabled = true
        })
        vm.errorLD.observe(viewLifecycleOwner, Observer { message ->
            message?.also { notifyError(it) }
        })
        vm.unsavedChangesLD.observe(viewLifecycleOwner, Observer { thereAreChanges ->
            buSave.isEnabled = thereAreChanges
        })
        vm.loadingLD.observe(viewLifecycleOwner, Observer { isLoading ->
            prBar.isVisible = isLoading
        })
        vm.cellUpdateLD.observe(viewLifecycleOwner, Observer { update ->
            updateCells(update)
        })
        vm.cellOptionsLD.observe(viewLifecycleOwner, Observer { cell ->
            cell?.also {
                suggestOptions(it)
                vm.notifyCellOptionsSuggested()
            }
        })
        vm.cellUpdateRequestLD.observe(viewLifecycleOwner, Observer { cell ->
            cell?.also {
                suggestUpdateCell(it)
                vm.notifyCellUpdateSuggested()
            }
        })

        view.setOnClickListener {
            vm.unselectCell()
        }
        buAddCell.setOnClickListener {
            addCell()
        }
        buSave.setOnClickListener {
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
        recvDays.adapter = ScheduleDaysAdapter(scheduleModel, vm)
    }

    private fun selectCell() {
        val adapter1 = recvCells.adapter as CellsAdapter? ?: return
        val adapter2 = recvDays.adapter as ScheduleDaysAdapter? ?: return
        val scale = if(selectedCellIndex < 0) 1.0f else 0.9f

        adapter1.selectCellAt(selectedCellIndex)
        adapter2.selectedCellIndex = selectedCellIndex
        recvDays.scaleX = scale
        recvDays.scaleY = scale
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


    private fun addCell() {
        val fragmMan = parentFragmentManager
        CellEditorDialog().apply {
            arguments = CellEditorDialog.arguments(-1, -1)
            show(fragmMan, null)
        }
    }


    private fun updateCells(update: CellUpdate?) {
        val cellsAdapter = recvCells.adapter as CellsAdapter? ?: return

        when(update) {
            is CellUpdate.Added -> {
                cellsAdapter.notifyCellProbablyAdded()
                vm.notifyCellUpdateConsumed()
                recvCells.scrollToPosition(cellsAdapter.itemCount - 1)
            }

            is CellUpdate.Updated -> {
                changeDataset(cellsAdapter)
            }

            is CellUpdate.Deleted -> {
                changeDataset(cellsAdapter)
            }
        }
    }


    private fun suggestOptions(cell: Cell) {
        val fragmMan = parentFragmentManager
        CellOptionsDialog().apply {
            arguments = CellOptionsDialog.arguments(cell)
            show(fragmMan, null)
        }
    }

    private fun suggestUpdateCell(cell: Cell) {
        val fragmMan = parentFragmentManager
        CellEditorDialog().apply {
            arguments = CellEditorDialog.arguments(cell.jailId, cell.number)
            show(fragmMan, null)
        }
    }

    private fun changeDataset(cellsAdapter: CellsAdapter) {
        cellsAdapter.notifyDataSetChanged()
        vm.notifyCellUpdateConsumed()

        val daysAdapter = recvDays.adapter as ScheduleDaysAdapter? ?: return
        daysAdapter.notifyDataSetChanged()
    }


    private fun dimen(dimenRes: Int): Int
        = resources.getDimensionPixelSize(dimenRes)
}