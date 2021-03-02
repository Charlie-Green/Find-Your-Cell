package by.zenkevich_churun.findcell.result.ui.shared.cppage.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import by.zenkevich_churun.findcell.domain.entity.CoPrisoner
import kotlinx.coroutines.*


/** Modification of [LiveData] for [CoPrisoner]s pages.
  * If emits a list of items together with a position of updated item.
  * It allows the UI to update only a specific item, if the item is known,
  * instead of entire list.
  * It also supports sorting items in case the position is not known
  * (that is, the entire list is updated) **/
class CoPrisonersPageLiveData(
    dataSource: LiveData< out List<CoPrisoner> >,
    private val scope: CoroutineScope,
    private val comparator: Comparator<CoPrisoner>?
): MediatorLiveData< Pair<List<CoPrisoner>, Int> >() {

    // ========================================================================
    // Fields and API:

    private var updatedPosition = -1


    fun setUpdatedPosition(position: Int) {
        updatedPosition = position
    }


    // ========================================================================
    // Building:

    init {
        addSource(dataSource) { list ->
            scope.launch(Dispatchers.IO) {
                submitList(list)
            }
        }
    }


    private fun submitList(newList: List<CoPrisoner>) {
        val position = updatedPosition
        updatedPosition = -1

        val resultList: MutableList<CoPrisoner>
        val sort: Boolean
        if(position < 0) {
            // Take the new list and sort it.
            resultList = newList.toMutableList()
            sort = true
        } else {
            // Only change the specific item.
            // Don't sort; otherwise it will be impossible to update 1 item.
            val tempList = (value?.first as MutableList<CoPrisoner>?)
                ?: newList.toMutableList()
            val itemUpdated = updateSingleItem(tempList, position, newList)

            resultList = if(itemUpdated) tempList else newList.toMutableList()
            sort = !itemUpdated
        }

        if(sort) {
            comparator?.also { resultList.sortWith(it) }
        }
        postValue( Pair(resultList, position) )
    }

    /** @return whether update succeeded. **/
    private fun updateSingleItem(
        dest: MutableList<CoPrisoner>,
        destIndex: Int,
        src: List<CoPrisoner>
    ): Boolean {

        if(destIndex !in dest.indices) {
            return false
        }

        val id = dest[destIndex].id
        val srcIndex = src.indexOfFirst { cp ->
            cp.id == id
        }
        if(srcIndex !in src.indices) {
            return false
        }

        dest[destIndex] = src[srcIndex]
        return true
    }
}