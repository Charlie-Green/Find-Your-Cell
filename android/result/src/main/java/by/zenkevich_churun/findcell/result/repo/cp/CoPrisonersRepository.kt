package by.zenkevich_churun.findcell.result.repo.cp

import android.content.Context
import androidx.lifecycle.LiveData
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CoPrisonersRepository @Inject constructor(
    @ApplicationContext private val appContext: Context ) {

    private var ldSuggested: LiveData< List<CoPrisoner> >? = null
    private var ldConnected: LiveData< List<CoPrisoner> >? = null
    private var ldRequests:  LiveData< List<CoPrisoner> >? = null


    /** [CoPrisoner]s with [CoPrisoner.Relation.SUGGESTED]
      * and [CoPrisoner.Relation.OUTCOMING_REQUEST]. **/
    fun suggestedLD(scope: CoroutineScope): LiveData< List<CoPrisoner> > {
        return ldSuggested ?: synchronized(this) {
            ldSuggested ?: CoPrisonersMediatorLiveData.Suggested(
                appContext,
                scope
            ).also { ldSuggested = it }
        }
    }


    /** [CoPrisoner]s with [CoPrisoner.Relation.CONNECTED]. **/
    fun connectedLD(scope: CoroutineScope): LiveData< List<CoPrisoner> > {
        return ldConnected ?: synchronized(this) {
            ldConnected ?: CoPrisonersMediatorLiveData.Connected(
                appContext,
                scope
            ).also { ldConnected = it }
        }
    }


    /** [CoPrisoner]s with [CoPrisoner.Relation.INCOMING_REQUEST]. **/
    fun requestsLD(scope: CoroutineScope): LiveData< List<CoPrisoner> > {
        return ldRequests ?: synchronized(this) {
            ldRequests ?: CoPrisonersMediatorLiveData.Requests(
                appContext,
                scope
            ).also { ldRequests = it }
        }
    }
}