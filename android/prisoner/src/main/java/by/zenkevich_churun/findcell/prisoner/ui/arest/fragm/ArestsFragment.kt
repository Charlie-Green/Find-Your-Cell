package by.zenkevich_churun.findcell.prisoner.ui.arest.fragm

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.zenkevich_churun.findcell.core.util.android.DialogUtil
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.arest.state.ArestsListState
import by.zenkevich_churun.findcell.prisoner.ui.arest.state.CreateOrUpdateArestState
import by.zenkevich_churun.findcell.prisoner.ui.arest.vm.ArestsViewModel
import by.zenkevich_churun.findcell.prisoner.ui.sched.fragm.ScheduleFragment
import kotlinx.android.synthetic.main.arests_fragm.*
import java.util.*
import javax.inject.Inject


class ArestsFragment: Fragment(R.layout.arests_fragm) {

    @Inject
    lateinit var vm: ArestsViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields()
        initRecycler()

        vm.loadData()
        vm.listStateLD.observe(viewLifecycleOwner, Observer { state ->
            renderState(state)
        })
        vm.openedArestLD.observe(viewLifecycleOwner, Observer { arest ->
            arest?.id?.also { id ->
                openSchedule(id)
                vm.notifyScheduleOpened()
            }
        })

        fabAdd.setOnClickListener { addArest() }
        restoreArestDateRangePicker()
    }


    private val adapter
        get() = recvArests.adapter as ArestsAdapter


    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = ArestsViewModel.get(appContext, this)
    }

    private fun initRecycler() {
        recvArests.layoutManager = LinearLayoutManager(requireContext())
        recvArests.adapter = ArestsAdapter(vm)
    }


    private fun renderState(state: ArestsListState) {
        when(state) {
            is ArestsListState.Loaded -> {
                val adapter = recvArests.adapter as ArestsAdapter
                adapter.submitList(state.arests)
            }

            is ArestsListState.NoInternet -> {
                notifyError(R.string.no_internet_title, R.string.arests_need_internet_msg) {
                    vm.notifyListStateConsumed()
                }
            }

            is ArestsListState.NetworkError -> {
                notifyError(R.string.error_title, R.string.get_arests_failed_msg) {
                    vm.notifyListStateConsumed()
                }
            }
        }
    }

    private fun renderState(state: CreateOrUpdateArestState) {
        when(state) {
            is CreateOrUpdateArestState.ArestsIntersectError -> {
                val msg = getString(
                    R.string.arests_intersect_msg,
                    ArestUiUtil.format(state.intersectedStart),
                    ArestUiUtil.format(state.intersectedEnd)
                )
                notifyCreateOrUpdateError(state.operationCreate, msg)

                vm.notifyAddOrUpdateStateConsumed()
            }

            is CreateOrUpdateArestState.NoInternet -> {
                notifyError(R.string.no_internet_title, R.string.arests_need_internet_msg) {
                    vm.notifyAddOrUpdateStateConsumed()
                }
            }

            is CreateOrUpdateArestState.NetworkError -> {
                notifyCreateOrUpdateError(
                    state.operationCreate,
                    getString(R.string.network_error_msg)
                )

                vm.notifyAddOrUpdateStateConsumed()
            }

            is CreateOrUpdateArestState.Created -> {
                adapter.notifyItemInserted(state.position)
                vm.notifyAddOrUpdateStateConsumed()
            }

            is CreateOrUpdateArestState.Updated -> {
                adapter.notifyItemMoved(state.oldPosition, state.newPosition)
                vm.notifyAddOrUpdateStateConsumed()
            }
        }
    }


    private fun openSchedule(arestId: Int) {
        val args = ScheduleFragment.arguments(arestId)
        findNavController().navigate(R.id.actOpenArest, args)
    }


    private fun notifyError(
        titleRes: Int,
        messageRes: Int,
        onDismiss: (DialogInterface) -> Unit ) {

        AlertDialog.Builder(requireContext())
            .setTitle(titleRes)
            .setMessage(messageRes)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }.setOnDismissListener(onDismiss)
            .show()
    }

    private fun notifyCreateOrUpdateError(
        operationCreate: Boolean,
        message: String ) {

        val titleRes =
            if(operationCreate) R.string.add_arest_failed_title
            else R.string.update_arest_failed_title

        AlertDialog.Builder(requireContext())
            .setTitle(titleRes)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }.setOnDismissListener {
                vm.notifyAddOrUpdateStateConsumed()
            }.show()
    }


    private fun addArest() {
        DialogUtil.pickDateRange(
            parentFragmentManager,
            Calendar.getInstance().apply { add(Calendar.DATE, -15) },
            Calendar.getInstance(),
            this::onArestDateRangeSelected
        )
    }

    private fun restoreArestDateRangePicker() {
        DialogUtil.restoreDateRangePicker(
            parentFragmentManager,
            this::onArestDateRangeSelected
        )
    }

    private fun onArestDateRangeSelected(start: Long, end: Long) {
       // TODO
    }
}