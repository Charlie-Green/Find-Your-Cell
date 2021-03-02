package by.zenkevich_churun.findcell.core.api.cp

import by.zenkevich_churun.findcell.domain.entity.CoPrisoner
import by.zenkevich_churun.findcell.domain.response.GetCoPrisonerResponse


interface CoPrisonersApi {

    /** Marks that either user A sent to the user B a connect request
      * or that A accepted B's request. (From the server's point of view, it is the same).
      * @return the new [CoPrisoner.Relation] between the two [Prisoner]s **/
    fun connect(
        prisonerId: Int,
        passwordHash: ByteArray,
        coPrisonerId: Int
    ): CoPrisoner.Relation

    /** Opposite to [connect].
      * @return same as [connect]. **/
    fun disconnect(
        prisonerId: Int,
        passwordHash: ByteArray,
        coPrisonerId: Int
    ): CoPrisoner.Relation

    fun getCoPrisoner(
        prisonerId: Int,
        passwordHash: ByteArray,
        coPrisonerId: Int
    ): GetCoPrisonerResponse
}