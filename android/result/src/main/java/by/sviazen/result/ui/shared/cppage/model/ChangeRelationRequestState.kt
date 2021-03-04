package by.sviazen.result.ui.shared.cppage.model


/** Defines states for a process of sending/canceling a connect request
  * with some (potential) [CoPrisoner]. **/
sealed class ChangeRelationRequestState {
    object Sending: ChangeRelationRequestState()

    class NetworkError: ChangeRelationRequestState() {
        var notified = false
    }

    class Success(
        val updatedPosition: Int,
        val message: String
    ): ChangeRelationRequestState() {
        var notified = false
    }
}