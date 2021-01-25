package by.zenkevich_churun.findcell.result.ui.shared.cps

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import by.zenkevich_churun.findcell.core.ui.common.SviazenFragment
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.databinding.CoprisonersPageBinding


/** The two pages are similar to each other
  * so this is the common parent class for them. **/
abstract class CoPrisonersPage<
    ViewModelType: CoPrisonersPageViewModel
>: SviazenFragment<CoprisonersPageBinding>() {

    private var positionNextUpdated = -1
    private var wasDataUpdated = false

    protected lateinit var vm: ViewModelType


    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = CoprisonersPageBinding.inflate(inflater)

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm = obtainViewModel(requireContext().applicationContext)

        val optionsAdapter = provideOptionsAdapter(vm)
        vb.recv.adapter = CoPrisonersRecyclerAdapter(vm, optionsAdapter)
        observeData(vm.dataLD)
        observeExpandedPosition(vm.expandedPositionLD)
    }


    /** Call this method when a partial update of [CoPrisoner]s list
      * is going on and the specific position of item to update gets known. **/
    protected fun notifyCoPrisonerChanged(position: Int) {
        positionNextUpdated = position
        optionallyUpdateCoPrisoner()
    }


    private fun observeData(ld: LiveData< Pair<List<CoPrisoner>, Boolean> >) {
        ld.observe(viewLifecycleOwner) { pair ->
            recyclerAdapter.submitData(pair.first, pair.second)
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


    protected val recyclerAdapter: CoPrisonersRecyclerAdapter
        get() = vb.recv.adapter as? CoPrisonersRecyclerAdapter
            ?: throw IllegalStateException("Adapter not set")


    protected abstract fun obtainViewModel(
        appContext: Context
    ): ViewModelType

    protected abstract fun provideOptionsAdapter(
        vm: ViewModelType
    ): CoPrisonerOptionsAdapter
}