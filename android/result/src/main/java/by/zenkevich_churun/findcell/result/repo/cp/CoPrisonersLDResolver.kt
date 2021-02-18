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


internal class CoPrisonersLDResolver(private val appContext: Context) {

    fun suggested(): LiveData< out List<CoPrisoner> > {
        return resolve(listOf(
            CoPrisoner.Relation.SUGGESTED,
            CoPrisoner.Relation.OUTCOMING_REQUEST
        ))
    }

    fun connected(): LiveData< out List<CoPrisoner> > {
        return resolve(listOf(
            CoPrisoner.Relation.CONNECTED
        ))
    }

    fun incomingRequests(): LiveData< out List<CoPrisoner> > {
        return resolve(listOf(
            CoPrisoner.Relation.INCOMING_REQUEST,
            CoPrisoner.Relation.REQUEST_DECLINED
        ))
    }

    fun outcomingRequests(): LiveData< out List<CoPrisoner> > {
        return resolve(listOf(
            CoPrisoner.Relation.OUTCOMING_REQUEST
        ))
    }


    private fun resolve(
        allowedRelations: List<CoPrisoner.Relation>
    ): LiveData< List<CoPrisonerEntity> > {

        val dao = CoPrisonersDatabase.get(appContext).dao
        return dao.coPrisonersLD(allowedRelations)
    }
}