package by.sviazen.result.ui.general.connect

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.sviazen.core.util.android.NavigationUtil
import by.sviazen.domain.entity.CoPrisoner
import by.sviazen.result.R
import by.sviazen.result.ui.contact.dialog.CoPrisonerContactsDialog
import by.sviazen.result.ui.shared.cppage.fragm.CoPrisonersPageDescriptor
import by.sviazen.result.ui.shared.cppage.fragm.CoPrisonersPageFragment


private class ConnectedPageDescriptor(
    private val owner: Fragment
): CoPrisonersPageDescriptor<ConnectedCoPrisonersViewModel> {

    override val emptyLabelRes: Int
        get() = R.string.cp_connected_empty


    override fun label1(cp: CoPrisoner): Int {
        return R.string.cpoption_view_contacts
    }

    override fun label2(cp: CoPrisoner): Int {
        return R.string.cpoption_disconnect
    }

    override fun onSelected1(
        vm: ConnectedCoPrisonersViewModel,
        cp: CoPrisoner,
        position: Int ) {

        NavigationUtil.navigateIfNotYet(
            owner.findNavController(),
            obtainDialogNav()
        ) { CoPrisonerContactsDialog.arguments(cp.id, cp.name) }
    }

    override fun onSelected2(
        vm: ConnectedCoPrisonersViewModel,
        cp: CoPrisoner,
        position: Int ) {

        vm.breakConnection(position)
    }


    override fun getViewModel(
        appContext: Context
    ) = ConnectedCoPrisonersViewModel.get(appContext, owner)


    private fun obtainDialogNav(): Int {
        val packName = owner.requireContext().packageName
        return owner
            .resources
            .getIdentifier("dialogCpContacts", "id", packName)
    }
}


class ConnectedCoPrisonersPage: CoPrisonersPageFragment<ConnectedCoPrisonersViewModel>() {

    override fun providePageDescriptor():
    CoPrisonersPageDescriptor<ConnectedCoPrisonersViewModel> {
        return ConnectedPageDescriptor(this)
    }
}