package by.zenkevich_churun.findcell.result.ui.general.connect

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelStoreOwner
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.R
import by.zenkevich_churun.findcell.result.ui.shared.cppage.fragm.CoPrisonersPageDescriptor
import by.zenkevich_churun.findcell.result.ui.shared.cppage.fragm.CoPrisonersPageFragment


private class ConnectedPageDescriptor(
    private val vmOwner: ViewModelStoreOwner
): CoPrisonersPageDescriptor<ConnectedCoPrisonersViewModel> {

    override val emptyLabelRes: Int
        get() = R.string.cp_connected_empty


    override fun label1(relation: CoPrisoner.Relation): Int {
        return R.string.cpoption_view_contacts
    }

    override fun label2(relation: CoPrisoner.Relation): Int {
        return R.string.cpoption_disconnect
    }

    override fun onSelected1(
        vm: ConnectedCoPrisonersViewModel,
        relation: CoPrisoner.Relation,
        position: Int ) {

        Log.v("CharlieDebug", "View contacts (position = $position)")
    }

    override fun onSelected2(
        vm: ConnectedCoPrisonersViewModel,
        relation: CoPrisoner.Relation,
        position: Int ) {

        vm.breakConnection(position)
    }


    override fun getViewModel(
        appContext: Context
    ) = ConnectedCoPrisonersViewModel.get(appContext, vmOwner)
}


class ConnectedCoPrisonersPage: CoPrisonersPageFragment<ConnectedCoPrisonersViewModel>() {

    override fun providePageDescriptor():
    CoPrisonersPageDescriptor<ConnectedCoPrisonersViewModel> {
        return ConnectedPageDescriptor(this)
    }
}