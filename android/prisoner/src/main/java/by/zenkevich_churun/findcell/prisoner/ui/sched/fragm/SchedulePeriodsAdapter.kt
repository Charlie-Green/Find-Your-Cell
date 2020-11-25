package by.zenkevich_churun.findcell.prisoner.ui.sched.fragm

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.ScheduleModel
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.SchedulePeriodModel
import by.zenkevich_churun.findcell.prisoner.util.view.period.SchedulePeriodView


internal class SchedulePeriodsAdapter(
    private val schedule: ScheduleModel
): RecyclerView.Adapter<SchedulePeriodsAdapter.PeriodViewHolder>() {

    class PeriodViewHolder(
        private val schedule: ScheduleModel,
        private val periodView: SchedulePeriodView
    ): RecyclerView.ViewHolder(periodView) {

        fun bind(period: SchedulePeriodModel) {
            val cell = schedule.cells[period.cellIndex]
            periodView.show(period, cell)
        }
    }


    override fun getItemCount(): Int
        = schedule.periods.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodViewHolder {
        val view = SchedulePeriodView(parent.context)
        return PeriodViewHolder(schedule, view)
    }

    override fun onBindViewHolder(holder: PeriodViewHolder, position: Int) {
        holder.bind( schedule.periods[position] )
    }
}