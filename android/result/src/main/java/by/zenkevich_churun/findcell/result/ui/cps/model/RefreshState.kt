package by.zenkevich_churun.findcell.result.ui.cps.model


/** This state determines whether refreshing process
  * is to be displayed on UI. **/
enum class RefreshState {
    NOT_REFRESHING,
    NO_INTERNET,
    REFRESHING,
    ERROR,
}