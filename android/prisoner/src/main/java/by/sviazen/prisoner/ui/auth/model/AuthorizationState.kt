package by.sviazen.prisoner.ui.auth.model


sealed class AuthorizationState {
    object Idle: AuthorizationState()
    object Loading: AuthorizationState()
    object InvalidUsername: AuthorizationState()
    object InvalidPassword: AuthorizationState()
    object PasswordNotConfirmed: AuthorizationState()
    object NoInternet: AuthorizationState()
    class UsernameTaken(val username: String): AuthorizationState()
    object WrongUsername: AuthorizationState()
    object WrongPassword: AuthorizationState()
    class NetworkError(val wasLoggingIn: Boolean): AuthorizationState()
    object Success: AuthorizationState()
}