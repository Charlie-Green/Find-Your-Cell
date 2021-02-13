package by.zenkevich_churun.findcell.result.repo.cp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.db.CoPrisonersDatabase
import by.zenkevich_churun.findcell.result.db.entity.CoPrisonerEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


internal sealed class CoPrisonersMediatorLiveData(
    private val appContext: Context,
    private val scope: CoroutineScope,
    source: LiveData< List<CoPrisonerEntity> >
): MediatorLiveData< List<CoPrisoner> >() {
    // ========================================================================
    // Implementation:

    constructor(
        appContext: Context,
        scope: CoroutineScope,
        allowedRelations: List<CoPrisoner.Relation>

    ): this(
        appContext,
        scope,
        CoPrisonersDatabase
            .get(appContext)
            .dao
            .coPrisonersLD(allowedRelations)
    )


    init {
        addSource(source) { cps ->
            scope.launch(Dispatchers.IO) {
                updateValue(cps)
            }
        }
    }


    private fun updateValue(cps: List<CoPrisonerEntity>) {
        val dao = CoPrisonersDatabase
            .get(appContext)
            .dao

        val compoundCoPrisoners = cps.map { cp ->
            val contacts = dao.contacts(cp.id)
            CompoundCoPrisoner(cp, contacts)
        }

        postValue(compoundCoPrisoners)
    }


    // ========================================================================
    // Sub-Classes:

    class Suggested(
        appContext: Context,
        scope: CoroutineScope

    ): CoPrisonersMediatorLiveData(
        appContext,
        scope,
        listOf(
            CoPrisoner.Relation.SUGGESTED,
            CoPrisoner.Relation.OUTCOMING_REQUEST
        )
    )

    class Connected(
        appContext: Context,
        scope: CoroutineScope

    ): CoPrisonersMediatorLiveData(
        appContext,
        scope,
        listOf(CoPrisoner.Relation.CONNECTED)
    )

    class Requests(
        appContext: Context,
        scope: CoroutineScope

    ): CoPrisonersMediatorLiveData(
        appContext,
        scope,
        listOf(CoPrisoner.Relation.INCOMING_REQUEST)
    )
}