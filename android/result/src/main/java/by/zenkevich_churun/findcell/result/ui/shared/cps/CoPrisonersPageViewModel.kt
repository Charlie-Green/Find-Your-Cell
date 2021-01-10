package by.zenkevich_churun.findcell.result.ui.shared.cps

import androidx.lifecycle.*
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner


/** Implements some functionality common for both
  * [CoPrisonersPage]s [ViewModel]s. **/
abstract class CoPrisonersPageViewModel: ViewModel() {

    private val mldExpandedPosition = MutableLiveData<Int>().apply {
        value = -1
    }

    /** Emits position of item within [CoPrisoner]s [List] whose state is expanded,
      * meaning options for this item are visible to the user. **/
    val expandedPositionLD: LiveData<Int>
        get() = mldExpandedPosition

    fun swapPositionExpandedStatus(position: Int) {
        mldExpandedPosition.value =
            if(mldExpandedPosition.value == position) -1
            else position
    }


    abstract val dataLD: LiveData< List<CoPrisoner> >
}