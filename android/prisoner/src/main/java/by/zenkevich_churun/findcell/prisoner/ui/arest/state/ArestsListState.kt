package by.zenkevich_churun.findcell.prisoner.ui.arest.state

import by.zenkevich_churun.findcell.core.entity.arest.Arest


sealed class ArestsListState {

    class Loaded(
        val arests: List<Arest>
    ): ArestsListState()

    object Idle: ArestsListState()
    object Loading: ArestsListState()

    class NetworkError: ArestsListState() {
        var consumed = false
    }

    class NoInternet: ArestsListState() {
        var consumed = false
    }
}