package by.zenkevich_churun.findcell.prisoner.ui.auth.model


enum class AuthorizationState {
    IDLE,
    LOADING,
    USERNAME_INVALID,
    PASSWORD_INVALID,
    NO_INTERNET,
    USERNAME_EXISTS,
    USERNAME_NOT_EXIST,
    PASSWORD_NOT_MATCH,
    NETWORK_ERROR_LOGIN,
    NETWORK_ERROR_SIGNUP,
    SUCCESS
}