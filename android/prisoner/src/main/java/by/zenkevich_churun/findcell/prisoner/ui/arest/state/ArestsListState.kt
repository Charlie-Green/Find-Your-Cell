package by.zenkevich_churun.findcell.prisoner.ui.arest.state

import by.zenkevich_churun.findcell.entity.entity.Arest


sealed class ArestsListState {

    class Loaded(
        val arests: List<Arest>
    ): ArestsListState()

    object Idle: ArestsListState()
    object Loading: ArestsListState()
    object NetworkError: ArestsListState()
    object NoInternet: ArestsListState()
}