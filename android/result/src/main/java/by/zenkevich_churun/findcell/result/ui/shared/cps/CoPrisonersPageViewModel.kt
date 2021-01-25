package by.zenkevich_churun.findcell.result.ui.shared.cps

import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner


/** Implements some functionality common for both
  * [CoPrisonersPage]s [ViewModel]s. **/
abstract class CoPrisonersPageViewModel: ViewModel() {

    private val mldExpandedPosition = MutableLiveData<Int>().apply {
        value = -1
    }

    protected var updateDataEntirely = true

    protected val mldData by lazy {
        createLiveData()
    }


    /** Emits position of item within [CoPrisoner]s [List] whose state is expanded,
      * meaning options for this item are visible to the user. **/
    val expandedPositionLD: LiveData<Int>
        get() = mldExpandedPosition

    /** Emits a pair (C; b) where:
      * - C is the list of [CoPrisoner]s to be shown to the user.
      * - b: if it's false, it means UI shouldn't update the list entirely.
      *   Instead, it will be provided with an exact
      *   item position (range of positions) to update. **/
    val dataLD: LiveData< Pair<List<CoPrisoner>, Boolean> >
        get() = mldData


    fun swapPositionExpandedStatus(position: Int) {
        mldExpandedPosition.value =
            if(mldExpandedPosition.value == position) -1
            else position
    }


    private fun createLiveData(): MediatorLiveData< Pair<List<CoPrisoner>, Boolean> > {
        val mld = MediatorLiveData< Pair<List<CoPrisoner>, Boolean> >()
        mld.addSource(dataSource) { data ->
            val resultPair = Pair(data, updateDataEntirely)
            AndroidUtil.setOrPost(mld, resultPair)
            updateDataEntirely = true
        }

        return mld
    }


    protected abstract val dataSource: LiveData< List<CoPrisoner> >
}