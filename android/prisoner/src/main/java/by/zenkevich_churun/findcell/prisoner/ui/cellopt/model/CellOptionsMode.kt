package by.zenkevich_churun.findcell.prisoner.ui.cellopt.model

import by.zenkevich_churun.findcell.entity.entity.Cell


/** The dialog can be at one of these modes at a moment.
  * The mode defines UI's visibility and functionality. **/
enum class CellOptionsMode {

    /** Suggesting the user to update or delete a [Cell] **/
    OPTIONS,

    /** Asking the user if they are sure to delete a [Cell]. **/
    CONFIRM_DELETE
}