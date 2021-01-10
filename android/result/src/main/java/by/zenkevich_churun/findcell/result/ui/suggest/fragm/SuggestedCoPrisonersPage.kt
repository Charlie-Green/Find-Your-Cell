package by.zenkevich_churun.findcell.result.ui.suggest.fragm

import android.os.Bundle
import android.view.View
import by.zenkevich_churun.findcell.result.ui.shared.cps.CoPrisonersPage
import by.zenkevich_churun.findcell.result.ui.suggest.vm.SuggestedCoPrisonersViewModel


class SuggestedCoPrisonersPage: CoPrisonersPage() {

    private lateinit var vm: SuggestedCoPrisonersViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()
        observe(vm.dataLD)
    }


    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = SuggestedCoPrisonersViewModel.get(appContext, this)
    }
}