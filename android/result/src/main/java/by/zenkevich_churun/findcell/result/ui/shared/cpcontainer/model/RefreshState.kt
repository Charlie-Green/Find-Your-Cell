package by.zenkevich_churun.findcell.result.ui.shared.cpcontainer.model


/** This state determines whether refreshing process
  * is to be displayed on UI. **/
sealed class RefreshState {

    object NotRefreshing: RefreshState()
    object InProgress: RefreshState()

    class NoInternet: RefreshState() {
        var notified = false
    }

    class NetworkError: RefreshState() {
        var notified = false
    }
}