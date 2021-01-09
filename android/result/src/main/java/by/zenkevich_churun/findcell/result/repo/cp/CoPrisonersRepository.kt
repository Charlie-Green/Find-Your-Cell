package by.zenkevich_churun.findcell.result.repo.cp

import android.content.Context
import androidx.lifecycle.LiveData
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CoPrisonersRepository @Inject constructor(
    @ApplicationContext private val appContext: Context ) {

    /** [CoPrisoner]s with [CoPrisoner.Relation.SUGGESTED]
     * and [CoPrisoner.Relation.OUTCOMING_REQUEST]. **/
    val suggestedLD: LiveData< List<CoPrisoner> > by lazy {
        CoPrisonersMediatorLiveData.Suggested(appContext)
    }

    /** [CoPrisoner]s with [CoPrisoner.Relation.CONNECTED]. **/
    val connectedLD: LiveData< List<CoPrisoner> > by lazy {
        CoPrisonersMediatorLiveData.Connected(appContext)
    }

    /** [CoPrisoner]s with [CoPrisoner.Relation.INCOMING_REQUEST]. **/
    val requestsLD: LiveData< List<CoPrisoner> > by lazy {
        CoPrisonersMediatorLiveData.Requests(appContext)
    }
}