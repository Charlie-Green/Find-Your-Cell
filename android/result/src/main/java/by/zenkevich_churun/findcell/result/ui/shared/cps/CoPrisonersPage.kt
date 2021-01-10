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


    private fun observeData(ld: LiveData< List<CoPrisoner> >) {
        ld.observe(viewLifecycleOwner) { cps ->
            adapter.submitData(cps)
        }
    }

    private fun observeExpandedPosition(ld: LiveData<Int>) {
        ld.observe(viewLifecycleOwner) { position ->
            adapter.expandedPosition = position
        }
    }


    private val adapter: CoPrisonersRecyclerAdapter
        get() = vb.recv.adapter as? CoPrisonersRecyclerAdapter
            ?: throw IllegalStateException("Adapter not set")


    protected abstract fun obtainViewModel(
        appContext: Context
    ): ViewModelType

    protected abstract fun provideOptionsAdapter(
        vm: ViewModelType
    ): CoPrisonerOptionsAdapter
}