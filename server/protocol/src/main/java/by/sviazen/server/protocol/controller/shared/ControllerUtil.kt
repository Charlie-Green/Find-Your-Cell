package by.sviazen.server.protocol.controller.shared

import by.sviazen.server.protocol.exc.IllegalServerParameterException


object ControllerUtil {

    /** Execute a piece of code catching [IllegalArgumentException].
      * If such an exception is caught, its message is printed
      * and [IllegalServerParameterException] is thrown. This way the client
      * gets the "Bad Request" response code instead of "Internal Server Error". **/
    inline fun <T> catchingIllegalArgument(
        pieceOfCode: () -> T
    ): T {

        try {
            return pieceOfCode()
        } catch(exc: IllegalArgumentException) {
            println("ILLEGAL ARGUMENT: ${exc.message}")
            throw IllegalServerParameterException()
        }
    }
}