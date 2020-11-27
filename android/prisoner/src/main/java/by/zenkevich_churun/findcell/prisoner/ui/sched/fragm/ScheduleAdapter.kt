package by.zenkevich_churun.findcell.prisoner.ui.sched.fragm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.ScheduleDayModel
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.ScheduleModel
import by.zenkevich_churun.findcell.prisoner.util.draw.MultipleColorsDrawable
import kotlinx.android.synthetic.main.schedule_day_item.view.*


internal class ScheduleAdapter(
    private val schedule: ScheduleModel
): RecyclerView.Adapter<ScheduleAdapter.DayViewHolder>() {

    class DayViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val frltRoot = itemView.frltRoot
        private val txtvData = itemView.txtvData

        fun bind(day: ScheduleDayModel) {
            frltRoot.background = MultipleColorsDrawable(day.backColors)
            txtvData.setTextColor(day.textColor)
            txtvData.text = day.dayData
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