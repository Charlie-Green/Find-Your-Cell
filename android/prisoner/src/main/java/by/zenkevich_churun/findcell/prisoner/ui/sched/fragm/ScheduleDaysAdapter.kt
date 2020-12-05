package by.zenkevich_churun.findcell.prisoner.ui.sched.fragm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.common.model.ScheduleDayModel
import by.zenkevich_churun.findcell.prisoner.ui.common.model.ScheduleModel
import by.zenkevich_churun.findcell.prisoner.ui.sched.vm.ScheduleViewModel
import by.zenkevich_churun.findcell.prisoner.util.draw.MultipleColorsDrawable
import kotlinx.android.synthetic.main.schedule_day_item.view.*


internal class ScheduleDaysAdapter(
    private val schedule: ScheduleModel,
    private val vm: ScheduleViewModel
): RecyclerView.Adapter<ScheduleDaysAdapter.DayViewHolder>() {

    var selectedCellIndex = -1


    inner class DayViewHolder(
        itemView: View
    ): RecyclerView.ViewHolder(itemView) {

        private val frltRoot = itemView.frltRoot
        private val txtvData = itemView.txtvData

        init {
            itemView.setOnClickListener {
                if(selectedCellIndex in schedule.cells.indices) {
                    reassignCell()
                }
            }
        }

        fun bind(day: ScheduleDayModel) {
            frltRoot.background = MultipleColorsDrawable(day.backColors)
            txtvData.setTextColor( textColor(day) )
            txtvData.text = day.dayData
        }

        private fun textColor(day: ScheduleDayModel): Int {
            if(day.textColor != ScheduleDayModel.UNDEFINED_COLOR) {
                return day.textColor
            }
            return AndroidUtil.themeColor(itemView.context, android.R.attr.textColor)
        }

        private fun reassignCell() {
            val day = schedule.dayAt(adapterPosition)
            schedule.markDayWithCell(selectedCellIndex, day.date)
            notifyItemChanged(adapterPosition)
            vm.notifyScheduleChanged()
        }
    }


    override fun getItemCount(): Int
        = schedule.dayCount

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.schedule_day_item, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind( schedule.dayAt(position) )
    }
}