package by.zenkevich_churun.findcell.core.injected.cp

import androidx.lifecycle.LiveData
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.entity.response.GetCoPrisonerResponse


interface CoPrisonersRepository {
    val suggestedLD: LiveData< out List<CoPrisoner> >
    val connectedLD: LiveData< out List<CoPrisoner> >
    val incomingRequestsLD: LiveData< out List<CoPrisoner> >
    val outcomingRequestsLD: LiveData< out List<CoPrisoner> >
    fun connect(coPrisonerId: Int): CoPrisoner.Relation?
    fun disconnect(coPrisonerId: Int): CoPrisoner.Relation?
    fun getPrisoner(id: Int): GetCoPrisonerResponse
    fun logOut()
}