package by.sviazen.core.repo.arest

import androidx.lifecycle.LiveData
import by.sviazen.domain.entity.Arest


interface ArestsRepository {

    /** List of [Arest]s to show to the user, wrapped into a [LiveData]. **/
    val arestsLD: LiveData< List<Arest> >


    /** In case of success, the new value is emitted by [arestsLD] **/
    fun arestsList(): GetArestsResult


    /** @return a pair of (response; int) where:
     *         - response notifies if the call succeeded;
     *         - int is list position of the newly created [Arest],
     *                 or any integer if the call failed., **/
    fun addArest(
        start: Long,
        end: Long
    ): AddOrUpdateArestResult


    /** @return [Collection] of list positions [Arest]s were deleted from,
     *         or null if deletion failed. **/
    fun deleteArests(ids: Collection<Int>): List<Int>?


    /** Clears current value of [arestsLD] **/
    fun clearArests()
}
