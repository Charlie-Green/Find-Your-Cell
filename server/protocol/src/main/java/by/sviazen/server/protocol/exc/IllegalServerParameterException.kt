package by.sviazen.server.protocol.exc

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(HttpStatus.BAD_REQUEST)
class IllegalServerParameterException: IllegalArgumentException {

    constructor():
        super()

    constructor(msg: String):
        super(msg)
    { println(msg) }
}