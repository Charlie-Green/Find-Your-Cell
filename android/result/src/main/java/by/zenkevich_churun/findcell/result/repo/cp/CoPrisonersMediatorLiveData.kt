package by.zenkevich_churun.findcell.result.repo.cp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.db.CoPrisonersDatabase
import by.zenkevich_churun.findcell.result.db.entity.CoPrisonerEntity


internal sealed class CoPrisonersMediatorLiveData(
    private val appContext: Context,
    source: LiveData< List<CoPrisonerEntity> >
): MediatorLiveData< List<CoPrisoner> >() {
    // ========================================================================
    // Implementation:

    constructor(appContext: Context, allowedRelations: List<CoPrisoner.Relation>):
        this(
            appContext,
            CoPrisonersDatabase.get(appContext).dao.coPrisonersLD(allowedRelations)
        )


    init {
        addSource(source) { cps ->
            updateValue(cps)
        }
    }


    private fun updateValue(cps: List<CoPrisonerEntity>) {
        val dao = CoPrisonersDatabase
            .get(appContext)
            .dao

        val compoundCoPrisoners = cps.map { cp ->
            CompoundCoPrisoner(
                cp,
                dao.contacts(cp.id)
            )
        }

        postValue(compoundCoPrisoners)
    }


    // ========================================================================
    // Sub-Classes:

    class Suggested(
        appContext: Context
    ): CoPrisonersMediatorLiveData(
        appContext,
        listOf(
            CoPrisoner.Relation.SUGGESTED,
            CoPrisoner.Relation.OUTCOMING_REQUEST
        )
    )

    class Connected(
        appContext: Context
    ): CoPrisonersMediatorLiveData(
        appContext,
        listOf(CoPrisoner.Relation.CONNECTED)
    )

    class Requests(
        appContext: Context
    ): CoPrisonersMediatorLiveData(
        appContext,
        listOf(CoPrisoner.Relation.INCOMING_REQUEST)
    )
}