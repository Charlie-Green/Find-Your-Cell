package by.zenkevich_churun.findcell.prisoner.ui.arest.state

import java.util.*


sealed class CreateOrUpdateArestState {
    object Idle:       CreateOrUpdateArestState()
    object NoInternet: CreateOrUpdateArestState()
    object Loading:    CreateOrUpdateArestState()

    class NetworkError(
        val operationCreate: Boolean
    ): CreateOrUpdateArestState()

    class ArestsIntersectError(
        val operationCreate: Boolean,
        val intersectedStart: Calendar,
        val intersectedEnd:   Calendar
    ): CreateOrUpdateArestState()

    class Success(
        val operationCreate: Boolean,
        val listPosition: Int
    ): CreateOrUpdateArestState()
}