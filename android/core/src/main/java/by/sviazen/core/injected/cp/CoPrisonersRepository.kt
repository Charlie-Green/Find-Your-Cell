package by.sviazen.core.injected.cp

import androidx.lifecycle.LiveData
import by.sviazen.domain.entity.CoPrisoner
import by.sviazen.domain.contract.cp.GetCoPrisonerResponse


interface CoPrisonersRepository {
    val suggestedLD: LiveData< out List<CoPrisoner> >
    val connectedLD: LiveData< out List<CoPrisoner> >
    val incomingRequestsLD: LiveData< out List<CoPrisoner> >
    val outcomingRequestsLD: LiveData< out List<CoPrisoner> >
    fun connect(coPrisonerId: Int): CoPrisoner.Relation?
    fun disconnect(coPrisonerId: Int): CoPrisoner.Relation?
    fun getPrisoner(id: Int): GetCoPrisonerResponse
}