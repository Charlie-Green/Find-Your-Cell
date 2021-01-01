package by.zenkevich_churun.findcell.prisoner.ui.arest.state

import by.zenkevich_churun.findcell.entity.entity.Arest


sealed class ArestsListState {

    object Idle: ArestsListState()
    object Loading: ArestsListState()

    class Loaded(
        val arests: List<Arest>
    ): ArestsListState() {

        var animated = false
    }

    class NetworkError: ArestsListState() {
        var notified = false
    }

    class NoInternet: ArestsListState() {
        var notified = false
    }
}