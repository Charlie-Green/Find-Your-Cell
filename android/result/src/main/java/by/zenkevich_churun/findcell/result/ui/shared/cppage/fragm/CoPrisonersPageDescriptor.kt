package by.zenkevich_churun.findcell.result.ui.shared.cppage.fragm

import android.content.Context
import androidx.lifecycle.ViewModel
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.ui.shared.cppage.vm.CoPrisonersPageViewModel


/** Decribes some specific implementation of [CoPrisonersPageFragment]
  * and resolves parameters specific to this implementation. **/
internal interface CoPrisonersPageDescriptor<ViewModelType: CoPrisonersPageViewModel> {

    /** Obtain [ViewModel] for the page. **/
    fun getViewModel(appContext: Context): ViewModelType

    /** String resource for the message to show to the user
      * if [CoPrisoner]s list is empty. **/
    val emptyLabelRes: Int


    /** For option buttons on a list item.
      * Defines label resource for the first button.
      * @param relation the value of [CoPrisoner.relation] property
      *        for the [CoPrisoner] the options are requested for. **/
    fun label1(relation: CoPrisoner.Relation): Int

    /** Same as [label1], but for the second option.
      * If only 1 option is supported, the method must return 0. **/
    fun label2(relation: CoPrisoner.Relation): Int {
        return 0
    }


    /** For option buttons on a list item.
      * Invoked when the first button is clicked.
      * @param relation the value of [CoPrisoner.relation] property
      *        for the [CoPrisoner] the options are requested for.
      * @param position adapter position of the item whose option is selected. **/
    fun onSelected1(relation: CoPrisoner.Relation, position: Int)

    /** Same as [onSelected1], but for the second button.
      * If [label2] returns 0, this method is never invoked. **/
    fun onSelected2(relation: CoPrisoner.Relation, position: Int) {
        throw NotImplementedError(
            "Either label2 must return 0 or this method must be implemented."
        )
    }
}