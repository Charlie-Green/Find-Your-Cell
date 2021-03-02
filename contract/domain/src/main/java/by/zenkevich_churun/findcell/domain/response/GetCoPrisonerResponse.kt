package by.zenkevich_churun.findcell.domain.response

import by.zenkevich_churun.findcell.domain.entity.Contact
import by.zenkevich_churun.findcell.domain.entity.Prisoner


sealed class GetCoPrisonerResponse {

    class Success(
        val contacts: List<Contact>,
        val info: String
    ): GetCoPrisonerResponse()

    /** The requested [Prisoner] turned out not connected
      * with the current [Prisoner], so access to their contacts was denied. **/
    object NotConnected: GetCoPrisonerResponse()

    object NetworkError: GetCoPrisonerResponse()
}