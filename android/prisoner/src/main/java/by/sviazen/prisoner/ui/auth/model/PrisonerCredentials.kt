package by.sviazen.prisoner.ui.auth.model


class PrisonerCredentials(
    val username: String,
    val password: String ) {

    val isUsernameValid: Boolean
        get() = (username.length >= 3 && !hasBorderWhitespaces(username))

    val isPasswordValid: Boolean
        get() = (password.length >= 3 && !hasBorderWhitespaces(password))


    private fun hasBorderWhitespaces(str: String): Boolean {
        return str[0].isWhitespace() || str.last().isWhitespace()
    }
}