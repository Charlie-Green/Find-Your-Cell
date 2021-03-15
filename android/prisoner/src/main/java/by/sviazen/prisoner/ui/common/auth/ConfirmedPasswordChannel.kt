package by.sviazen.prisoner.ui.common.auth

import kotlinx.coroutines.channels.Channel
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ConfirmedPasswordChannel @Inject constructor() {

    private val channel = Channel<String>()

    suspend fun send(confirmedPassword: String)
        = channel.send(confirmedPassword)

    suspend fun receive()
        = channel.receive()
}