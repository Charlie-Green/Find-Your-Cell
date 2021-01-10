package by.zenkevich_churun.findcell.result.ui.connect.fragm

import android.os.Bundle
import android.view.View
import by.zenkevich_churun.findcell.result.ui.connect.vm.ConnectedCoPrisonersViewModel
import by.zenkevich_churun.findcell.result.ui.shared.cps.CoPrisonersPage


class ConnectedCoPrisonersPage: CoPrisonersPage() {

    private lateinit var vm: ConnectedCoPrisonersViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()
        observe(vm.dataLD)
    }

    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = ConnectedCoPrisonersViewModel.get(appContext, this)
    }
}