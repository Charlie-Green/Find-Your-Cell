package by.sviazen.prisoner.ui.arest.fragm

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import by.sviazen.core.ui.common.SviazenFragment
import by.sviazen.core.util.android.NavigationUtil
import by.sviazen.prisoner.R
import by.sviazen.prisoner.databinding.ArestsFragmBinding
import by.sviazen.prisoner.ui.common.arest.ArestsListState
import by.sviazen.prisoner.ui.common.arest.CreateOrUpdateArestState
import by.sviazen.prisoner.ui.arest.state.DeleteArestsState
import by.sviazen.prisoner.ui.arest.vm.ArestsViewModel
import by.sviazen.prisoner.ui.sched.fragm.ScheduleFragment
import javax.inject.Inject


class ArestsFragment: SviazenFragment<ArestsFragmBinding>() {

    @Inject
    lateinit var vm: ArestsViewModel

    private lateinit var checksAnimer: ArestsCheckableStateAnimator


    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = ArestsFragmBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields()
        initRecycler()

        vm.loadData(false)
        vm.listStateLD.observe(viewLifecycleOwner, { state ->
            renderState(state)
        })
        vm.openedArestLD.observe(viewLifecycleOwner, { arest ->
            arest?.id?.also { id ->
                openSchedule(id)
                vm.notifyScheduleOpened()
            }
        })
        vm.addOrUpdateStateLD.observe(viewLifecycleOwner, { state ->
            renderState(state)
        })
        vm.deleteStateLD.observe(viewLifecycleOwner, { state ->
            renderState(state)
        })
        vm.loadingLD.observe(viewLifecycleOwner, { loading ->
            vb.prBar.isVisible = loading
        })

        vb.fabAdd.setOnClickListener   { addArest() }
        vb.buDelete.setOnClickListener { suggestDelete() }
        vb.buCancel.setOnClickListener { vm.cancelDelete() }
        vm.checkableLD.observe(viewLifecycleOwner) { checkable ->
            setCheckable(checkable)
        }
    }


    private val adapter
        get() = vb.recvArests.adapter as ArestsAdapter


    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = ArestsViewModel.get(appContext, this)
        checksAnimer = ArestsCheckableStateAnimator(
            vb.recvArests, vb.fabAdd, vb.buDelete, vb.buCancel )
    }

    private fun initRecycler() {
        vb.recvArests.adapter = ArestsAdapter(vm)
    }

    private fun animateRecycler() {
        vb.recvArests.layoutAnimation = AnimationUtils
            .loadLayoutAnimation(requireContext(), R.anim.layoutanim_arests)
    }


    private fun setCheckable(checkable: Boolean) {
        checksAnimer.setCheckable(checkable)  // Animate buttons in the bottom.
        vb.recvArests.post {
            adapter.isCheckable = checkable       // Animate item checkboxes.
        }
    }


    private fun renderState(state: ArestsListState) {
        vb.fabAdd.isVisible = state is ArestsListState.Loaded
        vb.txtvEmpty.isVisible =
            (state is ArestsListState.Loaded) &&
            (state.arests.isEmpty())

        when(state) {
            is ArestsListState.Loading -> {
                vb.vlltError.visibility = View.GONE
                // ProgressBar visibility is set in a standalone Observer.
            }

            is ArestsListState.Loaded -> {
                vb.vlltError.visibility = View.GONE

                if(!state.animated && !state.arests.isEmpty()) {
                    state.animated = true
                    animateRecycler()
                }

                val adapter = vb.recvArests.adapter as ArestsAdapter
                adapter.submitList(state.arests, state.checkedIds)
            }

            is ArestsListState.NoInternet -> {
                vb.vlltError.visibility = View.VISIBLE
                vb.buRetry.visibility = View.GONE
                vb.txtvError.setText(R.string.arests_need_internet_msg)

                if(!state.notified) {
                    showErrorDialog(R.string.no_internet_title, R.string.arests_need_internet_msg) {
                        state.notified = true
                    }
                }
            }

            is ArestsListState.NetworkError -> {
                vb.vlltError.visibility = View.VISIBLE
                vb.buRetry.visibility = View.VISIBLE
                vb.txtvError.setText(R.string.get_arests_failed_msg)

                if(!state.notified) {
                    notifyListStateNetworkError(state)
                }

                vb.buRetry.setOnClickListener {
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
                    showErrorDialog(R.string.no_internet_title, R.string.arests_need_internet_msg) {
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
                    vb.txtvEmpty.visibility = View.GONE
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

    private fun renderState(state: DeleteArestsState) {
        when(state) {
            is DeleteArestsState.Success -> {
                if(!state.notified) {
                    notifyArestsDeleted(state.positions)
                    state.notified = true
                }
            }

            is DeleteArestsState.NetworkError -> {
                if(!state.notified) {
                    showErrorDialog(
                        R.string.delete_arests_failed_title,
                        R.string.delete_arests_failed_msg
                    ) { state.notified = true }

                }
            }

            is DeleteArestsState.NoInternet -> {
                if(!state.notified) {
                    showErrorDialog(
                        R.string.no_internet_title,
                        R.string.delete_arests_needs_internet
                    ) { state.notified = true }

                }
            }
        }
    }


    private fun notifyListStateNetworkError(state: ArestsListState.NetworkError) {
        showErrorDialog(R.string.error_title, R.string.get_arests_failed_msg) {
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

    private fun notifyArestsDeleted(positions: List<Int>) {
        var countDeleted = 0
        for(position in positions) {
            adapter.notifyItemRemoved(position - countDeleted)
            ++countDeleted
        }
    }


    private fun openSchedule(arestId: Int) {
        val args = ScheduleFragment.arguments(arestId)
        findNavController().navigate(R.id.actOpenArest, args)
    }

    private fun suggestDelete() {
        val checks = adapter.checkedIds
        if(checks.isEmpty()) {
            return
        }

        val title = resources.getQuantityString(
            R.plurals.delete_arests_title, checks.size )
        val messageHtml1 = resources.getQuantityString(
            R.plurals.delete_arests_msg_1, checks.size, checks.size )
        val messageHtml2 = resources.getQuantityString(
            R.plurals.delete_arests_msg_2, checks.size, checks.size )
        val messageHtml = "$messageHtml1 $messageHtml2"

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage( HtmlCompat.fromHtml(messageHtml, 0) )
            .setPositiveButton(R.string.delete_yes) { dialog, _ ->
                vm.delete()
                dialog.dismiss()
            }.setNegativeButton(R.string.delete_no) { dialog, _ ->
                vm.cancelDelete()
                dialog.dismiss()
            }.show()
    }


    private fun notifyCreateOrUpdateError(
        operationCreate: Boolean,
        message: String,
        onDismiss: (DialogInterface) -> Unit ) {

        val titleRes =
            if(operationCreate) R.string.add_arest_failed_title
            else R.string.update_arest_failed_title
        showErrorDialog( getString(titleRes), message, onDismiss )
    }


    private fun addArest() {
        NavigationUtil.safeNavigate(
            findNavController(),
            R.id.fragmArests,
            R.id.dialogAddArest
        ) { null }
    }
}