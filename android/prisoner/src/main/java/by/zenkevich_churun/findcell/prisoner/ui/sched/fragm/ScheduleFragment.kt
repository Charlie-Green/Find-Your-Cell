package by.zenkevich_churun.findcell.prisoner.ui.sched.fragm

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
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
import java.text.SimpleDateFormat
import java.util.*


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
                displaySchedule(it)
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
        if(state is ScheduleCrudState.Loading) {
            vb.prBar.visibility = View.VISIBLE
            return
        }
        vb.prBar.visibility = View.GONE

        when(state) {
            is ScheduleCrudState.GetFailed -> {
                if(!state.notified) {
                    val msg = getString(R.string.get_schedule_failed_msg)
                    notifyError(msg) {
                        state.notified = true
                        findNavController().navigateUp()
                    }
                }
            }

            is ScheduleCrudState.UpdateFailed -> {
                if(!state.notified) {
                    val msg = getString(R.string.update_schedule_failed_msg)
                    notifyError(msg) {
                        state.notified = true
                    }
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
                if(!state.applied) {
                    state.applied = true

                    cellsAdapter?.notifyCellProbablyAdded()
                    val cellsCount = cellsAdapter?.itemCount ?: 1
                    vb.recvCells.scrollToPosition(cellsCount - 1)
                }
            }

            is ScheduleCellsCrudState.Updated -> {
                if(!state.applied) {
                    state.applied = true
                    notifyDataSetChanged()
                }
            }

            is ScheduleCellsCrudState.Editing.AddFailed,
            is ScheduleCellsCrudState.Editing.UpdateFailed -> {
                // Show the Edit dialog which renders the state.
                NavigationUtil.navigateIfNotYet(
                    findNavController(),
                    R.id.dialogCellEdit
                ) { null }
            }

            is ScheduleCellsCrudState.Deleted -> {
                if(!state.applied) {
                    state.applied = true
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun displaySchedule(scheduleModel: ScheduleModel) {
        displayDate(vb.lltDates.txtvStart, scheduleModel.start)
        displayDate(vb.lltDates.txtvEnd, scheduleModel.end)
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

    private fun notifyError(
        message: String,
        onDismiss: (DialogInterface) -> Unit

    ) = showErrorDialog(
        getString(R.string.error_title),
        message,
        onDismiss
    )

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

    private fun displayDate(txtv: TextView, time: Long) {
        txtv.text = dateFormat.format( Date(time) )
    }


    // ==================================================================================
    // Companion:

    companion object {
        @SuppressLint("SimpleDateFormat")  // No need for locale: only digits are used.
        private val dateFormat = SimpleDateFormat("dd.MM.yyyy")

        fun arguments(arestId: Int)
            = ScheduleArguments.createBundle(arestId)
    }
}