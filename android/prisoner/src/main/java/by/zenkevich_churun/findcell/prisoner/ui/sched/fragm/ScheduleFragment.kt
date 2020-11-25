package by.zenkevich_churun.findcell.prisoner.ui.sched.fragm

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.Fragment
import by.zenkevich_churun.findcell.core.entity.sched.Schedule
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.CellModel
import by.zenkevich_churun.findcell.prisoner.util.view.cell.CellView
import kotlinx.android.synthetic.main.schedule_fragm.*


/** Allows viewing interactive editing of the user's arest [Schedule]. **/
class ScheduleFragment: Fragment(R.layout.schedule_fragm) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cell = CellModel(
            "Окрестина ЦИП", 789,
            0xfff08000.toInt(),
            0xff004000.toInt(),
            0xff000000.toInt()
        )

        destination.setOnDragListener { _, event ->
            when(event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    Log.v("CharlieDebug", "Drag started.")
                }

                DragEvent.ACTION_DRAG_ENTERED -> {
                    Log.v("CharlieDebug", "Can accept!")
                }

                DragEvent.ACTION_DRAG_EXITED -> {
                    Log.v("CharlieDebug", "Drag exited. :(")
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    Log.v("CharlieDebug", "Drag ended.")
                }

                DragEvent.ACTION_DROP -> {
                    Log.v("CharlieDebug", "It's dropped.")
                }

                else -> {
                    Log.v("CharlieDebug", "Unknown event action ${event.action}")
                }
            }

            true  // We ARE interested in the event.
        }

        val cellView = CellView(requireContext()).apply {
            setBackgroundColor(cell.backColor)
            setNumberBackgroundColor(cell.numberBackColor)
            setTextColor(cell.textColor)
            show(cell)

            prepareForShadowBuilder(500, 150)
        }

        val clipItem = ClipData.Item(null as CharSequence?)
        val clipData = ClipData(null, arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), clipItem)
        val shadowBuilder = CellShadowBuilder(cellView)

        source.setOnLongClickListener {
            if(Build.VERSION.SDK_INT >= 24) {
                source.startDragAndDrop(clipData, shadowBuilder, null, 0)
            } else {
                source.startDrag(clipData, shadowBuilder, null, 0)
            }
        }
    }

//    private fun showCell(cell: CellModel) {
//        cellView.apply {
//            setBackgroundColor(cell.backColor)
//            setNumberBackgroundColor(cell.numberBackColor)
//            setTextColor(cell.textColor)
//            show(cell)
//        }
//    }
}