package by.sviazen.prisoner.ui.common.arest

import java.util.*


sealed class CreateOrUpdateArestState {
    object Idle:       CreateOrUpdateArestState()
    object Loading:    CreateOrUpdateArestState()

    class NoInternet: CreateOrUpdateArestState() {
        var notified = false
    }

    class NetworkError(
        val operationCreate: Boolean
    ): CreateOrUpdateArestState() {

        var notified = false
    }

    class ArestsIntersectError(
        val operationCreate: Boolean,
        val intersectedStart: Long,
        val intersectedEnd: Long
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