package by.zenkevich_churun.findcell.prisoner.ui.arest.state



sealed class DeleteArestsState {

    object Idle: DeleteArestsState()
    object InProgress: DeleteArestsState()

    class NoInternet: DeleteArestsState() {
        var notified = false
    }

    class NetworkError: DeleteArestsState() {
        var notified = false
    }

    class Success(
        /** Minimumm position of a deleted item. **/
        val minPosition: Int,

        /** Maximumm position of a deleted item. **/
        val maxPosition: Int
    ): DeleteArestsState() {

        var notified = false
    }
}