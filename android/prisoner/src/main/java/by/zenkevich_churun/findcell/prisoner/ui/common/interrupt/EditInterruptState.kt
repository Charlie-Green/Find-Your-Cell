package by.zenkevich_churun.findcell.prisoner.ui.common.interrupt

import androidx.navigation.NavDestination
import androidx.navigation.NavGraph


/** When the user attempts to navigate back while editing,
  * they are warned about potential unsaved changes.
  * This class is shared across the UI implementing this routine. **/
sealed class EditInterruptState {

    object NotRequested: EditInterruptState()

    class Asking(
        /** The value to pass to [Confirmed.source] if interrupt is confirmed. **/
        val source: Int,

        /** The value to pass to [Confirmed.dest] if interrupt is confirmed. **/
        val dest: Int
    ): EditInterruptState()

    class Confirmed(
        /** Current [NavDestination] ID at the moment of interrupt. **/
        val source: Int,

        /** A [NavGraph] ID to navigate to,
          * or 0 to navigate back. **/
        val dest: Int
    ): EditInterruptState()
}