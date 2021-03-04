package by.sviazen.result.ui.general.container

import android.content.Context
import androidx.lifecycle.ViewModelStoreOwner
import by.sviazen.result.R
import by.sviazen.result.ui.general.connect.ConnectedCoPrisonersPage
import by.sviazen.result.ui.general.suggest.SuggestedCoPrisonersPage
import by.sviazen.result.ui.shared.cpcontainer.fragm.CoPrisonersContainerDescriptor
import by.sviazen.result.ui.shared.cpcontainer.fragm.CoPrisonersContainerFragment


private const val INDEX_SUGGESTED = 0
private const val INDEX_CONNECTED = 1


private class GeneralContainerDescriptor(
    private val vmOwner: ViewModelStoreOwner
): CoPrisonersContainerDescriptor<CoPrisonersGeneralViewModel> {

    override val tabCount: Int
        get() = 2

    override fun tabLabelRes(index: Int) = when(index) {
        INDEX_SUGGESTED -> R.string.suggested_coprisoners_tabtext
        else            -> R.string.connected_coprisoners_tabtext
    }

    override fun createPage(index: Int) = when(index) {
        INDEX_SUGGESTED -> SuggestedCoPrisonersPage()
        else            -> ConnectedCoPrisonersPage()
    }

    override fun getViewModel(
        appContext: Context
    ) = CoPrisonersGeneralViewModel.get(appContext, vmOwner)
}


class CoPrisonersGeneralFragment: CoPrisonersContainerFragment<CoPrisonersGeneralViewModel>() {

    override fun provideContainerDescriptor():
    CoPrisonersContainerDescriptor<CoPrisonersGeneralViewModel> {
        return GeneralContainerDescriptor(this)
    }
}