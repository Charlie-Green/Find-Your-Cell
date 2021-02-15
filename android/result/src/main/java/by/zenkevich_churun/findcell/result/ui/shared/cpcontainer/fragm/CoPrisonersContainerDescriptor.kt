package by.zenkevich_churun.findcell.result.ui.shared.cpcontainer.fragm

import android.content.Context
import by.zenkevich_churun.findcell.result.ui.shared.cpcontainer.vm.CoPrisonersContainerViewModel
import by.zenkevich_churun.findcell.result.ui.shared.cppage.fragm.CoPrisonersPageFragment


/** Describes concrete parameter values
  * for some implementation of [CoPrisonersContainerFragment]. **/
internal interface CoPrisonersContainerDescriptor<
    ViewModelType: CoPrisonersContainerViewModel> {

    /** Number of tabs. **/
    val tabCount: Int

    /** String resource for the label of tab J (0 <= J < [tabCount]) **/
    fun tabLabelRes(index: Int): Int

    /** Create a [CoPrisonersPageFragment] described by tab J (0 <= J < [tabCount]). **/
    fun createPage(index: Int): CoPrisonersPageFragment<*>

    /** Get a [ViewModel] instance. **/
    fun getViewModel(appContext: Context): ViewModelType
}