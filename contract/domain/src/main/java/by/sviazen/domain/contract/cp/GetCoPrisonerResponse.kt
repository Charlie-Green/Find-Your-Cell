package by.sviazen.domain.contract.cp

import by.sviazen.domain.entity.Contact
import by.sviazen.domain.entity.Prisoner


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