package by.zenkevich_churun.findcell.prisoner.ui.common.interrupt


/** When the user attempts to navigate back while editing,
  * they are warned about potential unsaved changes.
  * This class is shared across the UI implementing this routine. **/
enum class EditInterruptState {
    NOT_REQUESTED,
    ASKING,
    CONFIRMED
}