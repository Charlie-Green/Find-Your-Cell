package by.sviazen.prisoner.ui.sched.fragm

import android.view.*
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import by.sviazen.core.util.android.AndroidUtil
import by.sviazen.prisoner.databinding.ScheduleDayItemBinding
import by.sviazen.prisoner.ui.common.sched.period.ScheduleDayModel
import by.sviazen.prisoner.ui.common.sched.period.ScheduleModel
import by.sviazen.prisoner.ui.sched.vm.ScheduleViewModel
import by.sviazen.prisoner.util.draw.MultipleColorsDrawable


internal class ScheduleDaysAdapter(
    private val schedule: ScheduleModel,
    private val vm: ScheduleViewModel
): RecyclerView.Adapter<ScheduleDaysAdapter.DayViewHolder>() {

    private var enabled = true
    var selectedCellIndex = -1


    /** [ScheduleDaysAdapter] is considered enabled if it displays the data.
      * If not enabled, the [RecyclerView] appears blank. **/
    var isEnabled: Boolean
        get() { return enabled }
        set(value) {
        if(enabled != value) {
            enabled = value
            notifyDataSetChanged()
        }
    }


    inner class DayViewHolder(
        private val vb: ScheduleDayItemBinding
    ): RecyclerView.ViewHolder(vb.root) {

        init {
            itemView.setOnClickListener {
                if(selectedCellIndex in schedule.cells.indices) {
                    reassignCell()
                }
            }
        }

        fun bind(day: ScheduleDayModel) {
            vb.root.background = MultipleColorsDrawable(day.backColors)
            vb.txtvData.setTextColor( textColor(day) )
            vb.txtvData.text = HtmlCompat.fromHtml(day.fullHtml, 0)
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
        = if(enabled) schedule.dayCount else 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val vb = ScheduleDayItemBinding.inflate(inflater, parent, false)
        return DayViewHolder(vb)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind( schedule.dayAt(position) )
    }
}