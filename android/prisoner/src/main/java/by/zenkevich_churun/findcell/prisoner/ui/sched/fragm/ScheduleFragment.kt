package by.zenkevich_churun.findcell.prisoner.ui.sched.fragm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.zenkevich_churun.findcell.core.ui.common.SviazenFragment
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.core.util.android.NavigationUtil
import by.zenkevich_churun.findcell.core.util.recycler.autogrid.AutomaticGridLayoutManager
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.databinding.ScheduleFragmBinding
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.ScheduleCellsCrudState
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.ScheduleModel
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.ScheduleCrudState
import by.zenkevich_churun.findcell.prisoner.ui.sched.vm.ScheduleViewModel


/** Allows viewing interactive editing of the user's arest [Schedule]. **/
class ScheduleFragment: SviazenFragment<ScheduleFragmBinding>() {
    // ==================================================================================
    // Fields:

    private lateinit var vm: ScheduleViewModel
    private var selectedCellIndex = -1


    // ==================================================================================
    // Lifecycle:

    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = ScheduleFragmBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields()
        initCellsAdapter()
        initDaysAdapter()
        loadSchedule()

        vm.selectedCellIndexLD.observe(viewLifecycleOwner, { cellIndex ->
            selectedCellIndex = cellIndex
            selectCell()
        })
        vm.scheduleStateLD.observe(viewLifecycleOwner, { state ->
            renderScheduleState(state)
        })
        vm.scheduleLD.observe(viewLifecycleOwner, { schedule ->
            schedule?.also {
                displaySchedule(schedule)
                selectCell()
                vb.buAddCell.isEnabled = true
            } ?: clearSchedule()
        })
        vm.unsavedChangesLD.observe(viewLifecycleOwner, { thereAreChanges ->
            vb.buSave.isEnabled = thereAreChanges
        })
        vm.cellCrudStateLD.observe(viewLifecycleOwner, { state ->
            renderCellState(state)
        })

        view.setOnClickListener {
            vm.unselectCell()
        }
        vb.buAddCell.setOnClickListener {
            vm.addCell()
        }
        vb.buSave.setOnClickListener {
            vm.saveSchedule()
        }
    }


    // ==================================================================================
    // Initialization:

    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = ScheduleViewModel.get(appContext, this)
    }

    private fun initCellsAdapter() {
        vb.recvCells.layoutManager = AutomaticGridLayoutManager(
            requireActivity(),
            dimen(R.dimen.cellview_width)
        )
    }

    private fun initDaysAdapter() {
        val activitySize = AndroidUtil.activitySize(requireActivity())
        vb.recvDays.pivotX = 0.5f*activitySize.width
        vb.recvDays.pivotY = 0.5f*activitySize.height

        val layoutMan = LinearLayoutManager(requireContext())
        val itemDecoration = DividerItemDecoration(requireContext(), layoutMan.orientation)
        vb.recvDays.apply {
            layoutManager = layoutMan
            addItemDecoration(itemDecoration)
        }
    }

    private fun loadSchedule() {
        val args = ScheduleArguments.of(this)
        vm.loadSchedule(args.arestId)
    }


    // ==================================================================================
    // Observers:

    private fun renderScheduleState(state: ScheduleCrudState) {
        if(state == ScheduleCrudState.LOADING) {
            vb.prBar.visibility = View.VISIBLE
            return
        }
        vb.prBar.visibility = View.GONE

        if(state.notified) {
            return
        }

        when(state) {
            ScheduleCrudState.GET_REQUIRES_INTERNET -> {
                notifyError( getString(R.string.get_schedule_needs_internet) ) {
                    state.notified = true
                }
            }

            ScheduleCrudState.GET_FAILED -> {
                notifyError( getString(R.string.get_schedule_failed_msg) ) {
                    state.notified = true
                }
            }

            ScheduleCrudState.UPDATE_FAILED -> {
                notifyError( getString(R.string.update_schedule_failed_msg) ) {
                    state.notified = true
                }
            }
        }
    }

    private fun renderCellState(state: ScheduleCellsCrudState) {
        when(state) {
            is ScheduleCellsCrudState.ViewingOptions -> {
                NavigationUtil.navigateIfNotYet(
                    findNavController(),
                    R.id.dialogCellOptions
                ) { null }
            }

            is ScheduleCellsCrudState.AddRequested,
            is ScheduleCellsCrudState.UpdateRequested -> {
                NavigationUtil.navigateIfNotYet(
                    findNavController(),
                    R.id.dialogCellEdit
                ) { null }
            }

            is ScheduleCellsCrudState.Added -> {
                if(!state.notified) {
                    state.notified = true

                    cellsAdapter?.notifyCellProbablyAdded()
                    val cellsCount = cellsAdapter?.itemCount ?: 1
                    vb.recvCells.scrollToPosition(cellsCount - 1)
                }
            }

            is ScheduleCellsCrudState.Updated -> {
                if(!state.notified) {
                    state.notified = true
                    notifyDataSetChanged()
                }
            }

            is ScheduleCellsCrudState.AddFailed,
            is ScheduleCellsCrudState.UpdateFailed -> {
                // Show the Edit dialog which renders the state.
                NavigationUtil.navigateIfNotYet(
                    findNavController(),
                    R.id.dialogCellEdit
                ) { null }
            }

            is ScheduleCellsCrudState.Deleted -> {
                if(!state.notified) {
                    state.notified = true
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun displaySchedule(scheduleModel: ScheduleModel) {
        vb.recvCells.adapter = CellsAdapter(scheduleModel.cells, vm)
        vb.recvDays.adapter = ScheduleDaysAdapter(scheduleModel, vm)
    }

    private fun clearSchedule() {
        vb.recvCells.adapter = CellsAdapter(listOf(), vm)
        daysAdapter?.isEnabled = false
    }

    private fun selectCell() {
        val adapter1 = vb.recvCells.adapter as CellsAdapter? ?: return
        val adapter2 = vb.recvDays.adapter as ScheduleDaysAdapter? ?: return
        val scale = if(selectedCellIndex < 0) 1.0f else 0.9f

        adapter1.selectCellAt(selectedCellIndex)
        adapter2.selectedCellIndex = selectedCellIndex
        vb.recvDays.scaleX = scale
        vb.recvDays.scaleY = scale
    }

    private fun notifyError(message: String, onDismiss: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.error_title)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }.setOnDismissListener {
                onDismiss()
            }.show()
    }

    private fun notifyDataSetChanged() {
        cellsAdapter?.notifyDataSetChanged()
        daysAdapter?.notifyDataSetChanged()
    }


    // ==================================================================================
    // Help:

    private val cellsAdapter
        get() = vb.recvCells.adapter as CellsAdapter?

    private val daysAdapter
        get() = vb.recvDays.adapter as ScheduleDaysAdapter?

    private fun dimen(dimenRes: Int): Int
        = resources.getDimensionPixelSize(dimenRes)


    // ==================================================================================
    // Companion:

    companion object {
        fun arguments(arestId: Int)
            = ScheduleArguments.createBundle(arestId)
    }
}