package by.zenkevich_churun.findcell.result.ui.shared.connect


sealed class ConnectRequestState {
    object Sending: ConnectRequestState()

    class NetworkError: ConnectRequestState() {
        var notified = false
    }

    class Success: ConnectRequestState() {
        var notified = false
    }
}