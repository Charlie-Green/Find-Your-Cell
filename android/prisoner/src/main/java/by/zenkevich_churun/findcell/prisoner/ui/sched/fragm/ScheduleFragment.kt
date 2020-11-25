package by.zenkevich_churun.findcell.prisoner.ui.sched.fragm

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.zenkevich_churun.findcell.core.entity.sched.Schedule
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.CellModel
import kotlinx.android.synthetic.main.schedule_fragm.*


/** Allows viewing interactive editing of the user's arest [Schedule]. **/
class ScheduleFragment: Fragment(R.layout.schedule_fragm) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val model = CellModel(
            "Окрестина ЦИП", 789,
            0xfff00000.toInt(),
            0xff800000.toInt(),
            Color.WHITE
        )

        showCell(model)
    }

    private fun showCell(cell: CellModel) {
        cellView.apply {
            setBackgroundColor(cell.backColor)
            setNumberBackgroundColor(cell.numberBackColor)
            setTextColor(cell.textColor)
            show(cell)
        }
    }
}