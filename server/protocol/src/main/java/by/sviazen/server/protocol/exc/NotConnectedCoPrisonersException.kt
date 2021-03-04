package by.sviazen.server.protocol.exc

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(HttpStatus.FORBIDDEN)
class NotConnectedCoPrisonersException(
    id1: Int,
    id2: Int
): IllegalArgumentException("Prisoners $id1 and $id2 are not connected") {

    init {
        println(message)
    }
}