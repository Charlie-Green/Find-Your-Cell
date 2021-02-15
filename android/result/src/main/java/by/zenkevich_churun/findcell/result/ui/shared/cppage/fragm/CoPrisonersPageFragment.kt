package by.zenkevich_churun.findcell.result.ui.shared.cppage.fragm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.CallSuper
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import by.zenkevich_churun.findcell.core.ui.common.SviazenFragment
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.databinding.CoprisonersPageBinding
import by.zenkevich_churun.findcell.result.ui.shared.cppage.vm.CoPrisonersPageViewModel


/** Common class for all [CoPrisoner]s pages, that is
  * lists of [CoPrisoner]s filtered by [CoPrisoner.Relation] **/
abstract class CoPrisonersPageFragment<
    ViewModelType: CoPrisonersPageViewModel
> internal constructor(
    private val pageDescriptor: CoPrisonersPageDescriptor<ViewModelType>
): SviazenFragment<CoprisonersPageBinding>() {

    private var positionNextUpdated = -1
    private var wasDataUpdated = false
    protected lateinit var vm: ViewModelType


    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = CoprisonersPageBinding.inflate(inflater)

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields()

        vb.recv.adapter = CoPrisonersRecyclerAdapter(vm, pageDescriptor)
        observeData(vm.dataLD)
        observeExpandedPosition(vm.expandedPositionLD)

        vb.txtvEmpty.setText(pageDescriptor.emptyLabelRes)
    }


    /** Call this method when a partial update of [CoPrisoner]s list
     * is going on and the specific position of item to update gets known. **/
    protected fun notifyCoPrisonerChanged(position: Int) {
        positionNextUpdated = position
        optionallyUpdateCoPrisoner()
    }


    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = pageDescriptor.getViewModel(appContext)
    }


    private fun observeData(ld: LiveData<Pair<List<CoPrisoner>, Boolean>>) {
        ld.observe(viewLifecycleOwner) { pair ->
            recyclerAdapter.submitData(pair.first, pair.second)
            vb.txtvEmpty.isVisible = pair.first.isEmpty()

            if(!pair.second) {
                wasDataUpdated = true
                optionallyUpdateCoPrisoner()
            }
        }
    }

    private fun observeExpandedPosition(ld: LiveData<Int>) {
        ld.observe(viewLifecycleOwner) { position ->
            recyclerAdapter.expandedPosition = position
        }
    }


    private fun optionallyUpdateCoPrisoner() {
        if(!wasDataUpdated || positionNextUpdated < 0) {
            return
        }

        // Update only when both the data is fresh
        // and the position to update is known:
        recyclerAdapter.notifyItemChanged(positionNextUpdated)

        // Reset to correctly handle next partial updates:
        positionNextUpdated = -1
        wasDataUpdated = false
    }


    private val recyclerAdapter: CoPrisonersRecyclerAdapter
        get() = vb.recv.adapter as? CoPrisonersRecyclerAdapter
            ?: throw IllegalStateException("Adapter not set")
}