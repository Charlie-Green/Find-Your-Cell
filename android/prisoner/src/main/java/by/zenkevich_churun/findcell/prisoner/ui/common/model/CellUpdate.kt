package by.zenkevich_churun.findcell.prisoner.ui.common.model


/** Notification about an update in [CellEditorState]'s cells list. **/
sealed class CellUpdate {
    object Added: CellUpdate()
    object Updated: CellUpdate()
    object Deleted: CellUpdate()
    object DeleteFailed: CellUpdate()
}