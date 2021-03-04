package by.sviazen.server.protocol.controller.auth

import by.sviazen.server.protocol.exc.IllegalServerParameterException


internal object AuthorizationValidator {

    fun validateCredentials(
        username: String,
        name: String? ) {

        assertLength(username, "username", 3)
        assertWhitespaces(username, "Username")

        if(name != null) {
            assertLength(name, "name", 1)
            assertWhitespaces(name, "Name")
        }
    }


    private fun assertLength(str: String, type: String, minLen: Int) {
        if(str.length < minLen) {
            throwIllegalParameter("Too short $type")
        }
    }

    private fun assertWhitespaces(str: String, type: String) {
        if(str.isEmpty()) {
            return
        }

        if(str[0].isWhitespace()) {
            throwIllegalParameter("$type has leading whitespaces")
        }
        if(str.last().isWhitespace()) {
            throwIllegalParameter("$type has trailing whitespaces")
        }
    }


    private fun throwIllegalParameter(message: String?): Nothing {
        val exc = message?.let {
            IllegalServerParameterException(it)
        } ?: IllegalServerParameterException()
        throw exc
    }
}