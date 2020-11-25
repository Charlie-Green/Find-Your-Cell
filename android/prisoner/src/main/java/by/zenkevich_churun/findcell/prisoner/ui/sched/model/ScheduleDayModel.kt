package by.zenkevich_churun.findcell.prisoner.ui.sched.model

import android.graphics.Color
import java.util.*


class ScheduleDayModel(
    val date: Calendar,
    var cellData: String,
    var textColor: Int ) {

    constructor(date: Calendar):
        this(date, "", UNDEFINED_COLOR)

    fun changeCell(cell: CellModel) {
        textColor = cell.textColor
        cellData = cell.toString()
    }


    companion object {
        /** In this case, use the theme-defined text color. **/
        const val UNDEFINED_COLOR = Color.TRANSPARENT
    }
}