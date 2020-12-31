package by.zenkevich_churun.findcell.prisoner.ui.arest.state

import java.util.*


sealed class CreateOrUpdateArestState {
    object Idle:       CreateOrUpdateArestState()
    object Loading:    CreateOrUpdateArestState()

    class NoInternet(): CreateOrUpdateArestState() {
        var notified = false
    }

    class NetworkError(
        val operationCreate: Boolean
    ): CreateOrUpdateArestState() {

        var notified = false
    }

    class ArestsIntersectError(
        val operationCreate: Boolean,
        val intersectedStart: Calendar,
        val intersectedEnd:   Calendar
    ): CreateOrUpdateArestState() {

        var notified = false
    }

    class Created(
        val position: Int
    ): CreateOrUpdateArestState() {

        var notified = false
    }

    class Updated(
        val oldPosition: Int,
        val newPosition: Int
    ): CreateOrUpdateArestState() {

        var notified = false
    }
}