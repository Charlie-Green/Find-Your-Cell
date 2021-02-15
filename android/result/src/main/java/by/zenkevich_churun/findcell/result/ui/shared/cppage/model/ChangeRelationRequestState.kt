package by.zenkevich_churun.findcell.result.ui.shared.cppage.model

import by.zenkevich_churun.findcell.entity.entity.CoPrisoner


/** Defines states for a process of sending/canceling a connect request
  * with some (potential) [CoPrisoner]. **/
sealed class ChangeRelationRequestState {
    object Sending: ChangeRelationRequestState()

    class NetworkError: ChangeRelationRequestState() {
        var notified = false
    }

    class Success(
        val updatedPosition: Int
    ): ChangeRelationRequestState() {
        var notified = false
    }
}