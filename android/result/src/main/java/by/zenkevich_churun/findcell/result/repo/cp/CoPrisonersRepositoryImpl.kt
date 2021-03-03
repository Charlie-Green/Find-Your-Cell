package by.zenkevich_churun.findcell.result.repo.cp

import android.content.Context
import android.util.Log
import by.zenkevich_churun.findcell.core.api.cp.CoPrisonersApi
import by.zenkevich_churun.findcell.core.common.prisoner.PrisonerStorage
import by.zenkevich_churun.findcell.core.injected.cp.CoPrisonersRepository
import by.zenkevich_churun.findcell.domain.entity.CoPrisoner
import by.zenkevich_churun.findcell.domain.entity.Prisoner
import by.zenkevich_churun.findcell.domain.contract.cp.GetCoPrisonerResponse
import by.zenkevich_churun.findcell.result.db.CoPrisonersDatabase
import by.zenkevich_churun.findcell.result.db.dao.CoPrisonersDao
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CoPrisonersRepositoryImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val prisonerStore: PrisonerStorage,
    private val cpApi: CoPrisonersApi
): CoPrisonersRepository {

    private val ldResolver = CoPrisonersLDResolver(appContext)


    /** [CoPrisoner]s with [CoPrisoner.Relation.SUGGESTED]
      * and [CoPrisoner.Relation.OUTCOMING_REQUEST]. **/
    override val suggestedLD by lazy {
        ldResolver.suggested()
    }

    /** [CoPrisoner]s with [CoPrisoner.Relation.CONNECTED]. **/
    override val connectedLD by lazy {
        ldResolver.connected()
    }


    /** [CoPrisoner]s with [CoPrisoner.Relation.INCOMING_REQUEST]. **/
    override val incomingRequestsLD by lazy {
        ldResolver.incomingRequests()
    }

    /** [CoPrisoner]s with [CoPrisoner.Relation.OUTCOMING_REQUEST]. **/
    override val outcomingRequestsLD by lazy {
        ldResolver.outcomingRequests()
    }


    /** @return the new [CoPrisoner.Relation], or null if network request failed. **/
    override fun connect(
        coPrisonerId: Int
    ): CoPrisoner.Relation? = sendRequest(coPrisonerId) { prisoner ->

        cpApi.connect(
            prisoner.id,
            prisoner.passwordHash!!,
            coPrisonerId
        )
    }

    /** @return the new [CoPrisoner.Relation], or null if network request failed. **/
    override fun disconnect(
        coPrisonerId: Int
    ): CoPrisoner.Relation? = sendRequest(coPrisonerId) { prisoner ->

        cpApi.disconnect(
            prisoner.id,
            prisoner.passwordHash!!,
            coPrisonerId
        )
    }


    override fun getPrisoner(id: Int): GetCoPrisonerResponse {
        val prisoner = prisonerStore.prisonerLD.value
            ?: throw IllegalStateException("Not authorized")

        try {
            // TODO: Alter database in case of NotConnected response.
            return cpApi
                .getCoPrisoner(prisoner.id, prisoner.passwordHash!!, id)
        } catch(exc: IOException) {
            return GetCoPrisonerResponse.NetworkError
        }
    }


    private val dao: CoPrisonersDao
        get() = CoPrisonersDatabase.get(appContext).dao

    private inline fun sendRequest(
        cpId: Int,
        doNetworkCall: (Prisoner) -> CoPrisoner.Relation
    ): CoPrisoner.Relation? {

        val prisoner = prisonerStore.prisonerLD.value ?: return null

        val newRelation = try {
            doNetworkCall(prisoner)
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to send connect request: ${exc.javaClass.name}: ${exc.message}")
            return null
        }

        dao.updateRelation(cpId, newRelation)
        return newRelation
    }


    companion object {
        private const val LOGTAG = "FindCell-CoPrisoner"
    }
}