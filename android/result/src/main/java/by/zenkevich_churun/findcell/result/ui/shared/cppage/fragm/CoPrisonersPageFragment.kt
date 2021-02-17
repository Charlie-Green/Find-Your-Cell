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
>: SviazenFragment<CoprisonersPageBinding>() {

    private lateinit var pageDescriptor: CoPrisonersPageDescriptor<ViewModelType>
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


    private fun initFields() {
        pageDescriptor = providePageDescriptor()
        val appContext = requireContext().applicationContext
        vm = pageDescriptor.getViewModel(appContext)
    }


    private fun observeData(ld: LiveData< Pair<List<CoPrisoner>, Int> >) {
        ld.observe(viewLifecycleOwner) { pair ->
            vb.txtvEmpty.isVisible = pair.first.isEmpty()
            recyclerAdapter.submitData(pair.first, pair.second)
        }
    }

    private fun observeExpandedPosition(ld: LiveData<Int>) {
        ld.observe(viewLifecycleOwner) { position ->
            recyclerAdapter.expandedPosition = position
        }
    }


    private val recyclerAdapter: CoPrisonersRecyclerAdapter<ViewModelType>
        get() = vb.recv.adapter as? CoPrisonersRecyclerAdapter<ViewModelType>
            ?: throw IllegalStateException("Adapter not set")


    internal abstract
    fun providePageDescriptor(): CoPrisonersPageDescriptor<ViewModelType>
}