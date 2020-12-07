
package by.zenkevich_churun.findcell.prisoner.ui.common.model

import android.graphics.Color
import java.util.*


class ScheduleDayModel(
    val date: Calendar,
    val dayData: String,
    val textColor: Int,
    val backColors: List<Int> ) {


    companion object {
        /** In this case, use the theme-defined text color. **/
        const val UNDEFINED_COLOR = Color.TRANSPARENT
    }
}