package by.zenkevich_churun.findcell.prisoner.ui.arest.fragm

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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

    private lateinit var checksAnimer: ArestsCheckableStateAnimator


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields()
        initRecycler()

        vm.loadData(false)
        vm.listStateLD.observe(viewLifecycleOwner, Observer { state ->
            renderState(state)
        })
        vm.openedArestLD.observe(viewLifecycleOwner, Observer { arest ->
            arest?.id?.also { id ->
                openSchedule(id)
                vm.notifyScheduleOpened()
            }
        })
        vm.addOrUpdateStateLD.observe(viewLifecycleOwner, Observer { state ->
            renderState(state)
        })
        vm.loadingLD.observe(viewLifecycleOwner, Observer { loading ->
            prBar.isVisible = loading
        })

        val layoutListener = object: View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View?,
                left: Int, top: Int, right: Int, bottom: Int,
                oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int ) {

                recvArests.removeOnLayoutChangeListener(this)
                observeCheckableState(bottom - top)
            }
        }
        recvArests.addOnLayoutChangeListener(layoutListener)

        fabAdd.setOnClickListener   { addArest() }
        buDelete.setOnClickListener { vm.delete() }
        buCancel.setOnClickListener { vm.cancelDelete() }

        restoreArestDateRangePicker()
    }


    private val adapter
        get() = recvArests.adapter as ArestsAdapter


    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = ArestsViewModel.get(appContext, this)
    }

    private fun initRecycler() {
        recvArests.adapter = ArestsAdapter(vm)
    }

    private fun animateRecycler() {
        recvArests.layoutAnimation = AnimationUtils
            .loadLayoutAnimation(requireContext(), R.anim.layoutanim_arests)
    }


    private fun observeCheckableState(screenHeight: Int) {
        val listMargin = resources.getDimensionPixelSize(R.dimen.arests_screen_padding)
        checksAnimer = ArestsCheckableStateAnimator
            .Builder(screenHeight, buDelete, buCancel)
            .setContentView(recvArests, listMargin)
            .setFadingView(fabAdd)
            .build()

        vm.checkableLD.observe(viewLifecycleOwner, Observer { checkable ->
            checksAnimer.setCheckable(checkable)  // Animate buttons in the bottom.
            recvArests.post {
                adapter.isCheckable = checkable       // Animate item checkboxes.
            }
        })
    }


    private fun renderState(state: ArestsListState) {
        fabAdd.isVisible = state is ArestsListState.Loaded

        when(state) {
            is ArestsListState.Loading -> {
                vlltError.visibility = View.GONE
                // ProgressBar visibility is set in a standalone Observer.
            }

            is ArestsListState.Loaded -> {
                vlltError.visibility = View.GONE

                if(!state.animated) {
                    state.animated = true
                    animateRecycler()
                }

                val adapter = recvArests.adapter as ArestsAdapter
                adapter.submitList(state.arests)
            }

            is ArestsListState.NoInternet -> {
                vlltError.visibility = View.VISIBLE
                buRetry.visibility = View.GONE
                txtvError.setText(R.string.arests_need_internet_msg)

                if(!state.notified) {
                    notifyError(R.string.no_internet_title, R.string.arests_need_internet_msg) {
                        state.notified = true
                    }
                }
            }

            is ArestsListState.NetworkError -> {
                vlltError.visibility = View.VISIBLE
                buRetry.visibility = View.VISIBLE
                txtvError.setText(R.string.get_arests_failed_msg)

                if(!state.notified) {
                    notifyListStateNetworkError(state)
                }

                buRetry.setOnClickListener {
                    vm.loadData(true)
                }
            }
        }
    }

    private fun renderState(state: CreateOrUpdateArestState) {
        when(state) {
            is CreateOrUpdateArestState.ArestsIntersectError -> {
                if(!state.notified) {
                    notifyArestsIntersect(state)
                }
            }

            is CreateOrUpdateArestState.NoInternet -> {
                if(!state.notified) {
                    notifyError(R.string.no_internet_title, R.string.arests_need_internet_msg) {
                        state.notified = true
                    }
                }
            }

            is CreateOrUpdateArestState.NetworkError -> {
                if(!state.notified) {
                    notifyCreateOrUpdateError(
                        state.operationCreate,
                        getString(R.string.network_error_msg)
                    ) { state.notified = true }
                }
            }

            is CreateOrUpdateArestState.Created -> {
                if(!state.notified) {
                    adapter.notifyItemInserted(state.position)
                    state.notified = true
                }
            }

            is CreateOrUpdateArestState.Updated -> {
                if(!state.notified) {
                    adapter.notifyItemMoved(state.oldPosition, state.newPosition)
                    state.notified = true
                }

            }
        }
    }

    private fun notifyListStateNetworkError(state: ArestsListState.NetworkError) {
        notifyError(R.string.error_title, R.string.get_arests_failed_msg) {
            state.notified = true
        }
    }

    private fun notifyArestsIntersect(state: CreateOrUpdateArestState.ArestsIntersectError) {
        val msg = getString(
            R.string.arests_intersect_msg,
            ArestUiUtil.format(state.intersectedStart),
            ArestUiUtil.format(state.intersectedEnd)
        )

        notifyCreateOrUpdateError(state.operationCreate, msg) {
            state.notified = true
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
        message: String,
        onDismiss: (DialogInterface) -> Unit ) {

        val titleRes =
            if(operationCreate) R.string.add_arest_failed_title
            else R.string.update_arest_failed_title

        AlertDialog.Builder(requireContext())
            .setTitle(titleRes)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }.setOnDismissListener(onDismiss)
            .show()
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
       vm.addArest(start, end)
    }
}