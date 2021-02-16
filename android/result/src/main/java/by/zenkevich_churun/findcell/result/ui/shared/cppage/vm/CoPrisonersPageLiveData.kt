package by.zenkevich_churun.findcell.result.ui.shared.cppage.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/** Modification of [LiveData] for [CoPrisoner]s pages.
  * If emits a list of items together with a position of updated item.
  * It allows the UI to update only a specific item, if the item is known,
  * instead of entire list.
  * It also supports sorting items in case the position is not known
  * (that is, the entire list is updated) **/
class CoPrisonersPageLiveData(
    dataSource: LiveData< List<CoPrisoner> >,
    private val scope: CoroutineScope,
    private val comparator: Comparator<CoPrisoner>?
): MediatorLiveData< Pair<List<CoPrisoner>, Int> >() {

    // ========================================================================
    // Fields and API:

    private var updatedPosition = -1


    fun setUpdatedPosition(position: Int) {
        updatedPosition = position
    }

    fun sort() {
        val list = this.value?.first ?: return
        val comparator = this.comparator ?: return
        updatedPosition = -1  // Old position will be invalid after the list is sorted.

        scope.launch(Dispatchers.IO) {
            val sortedList = list.sortedWith(comparator)
            postValue( Pair(sortedList, -1) )
        }
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

        val sortedList: MutableList<CoPrisoner>
        if(position < 0) {
            // Take the new list and sort it.
            sortedList = newList.toMutableList()
            comparator?.also { sortedList.sortWith(it) }
        } else {
            // Only change the specific item.
            // Don't sort; otherwise it will be impossible to update 1 item.
            sortedList = (value?.first as MutableList<CoPrisoner>?)
                ?: newList.toMutableList()
            updateSingleItem(sortedList, position, newList)
        }

        postValue( Pair(sortedList, position) )
    }

    private fun updateSingleItem(
        dest: MutableList<CoPrisoner>,
        destIndex: Int,
        src: List<CoPrisoner> ) {

        if(destIndex !in dest.indices) {
            return
        }

        val id = dest[destIndex].id
        val srcIndex = src.indexOfFirst { cp ->
            cp.id == id
        }
        if(destIndex !in dest.indices) {
            return
        }

        dest[destIndex] = src[srcIndex]
    }
}