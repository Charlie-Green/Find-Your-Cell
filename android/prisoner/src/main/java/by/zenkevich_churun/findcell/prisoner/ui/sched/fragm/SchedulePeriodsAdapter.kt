package by.zenkevich_churun.findcell.prisoner.ui.sched.fragm

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.ScheduleModel
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.SchedulePeriodModel
import by.zenkevich_churun.findcell.prisoner.util.view.period.SchedulePeriodResizedListener
import by.zenkevich_churun.findcell.prisoner.util.view.period.SchedulePeriodView


internal class SchedulePeriodsAdapter(
    private val schedule: ScheduleModel
): RecyclerView.Adapter<SchedulePeriodsAdapter.PeriodViewHolder>() {

    inner class PeriodViewHolder(
        private val periodView: SchedulePeriodView
    ): RecyclerView.ViewHolder(periodView) {

        init {
            listenToResize()
        }

        fun bind(period: SchedulePeriodModel) {
            val cell = schedule.cells[period.cellIndex]
            periodView.show(period, cell)
        }

        private fun listenToResize() {
            periodView.listenToResize( object: SchedulePeriodResizedListener {
                override fun onBeginningCollapsed() {
                    safeCollapse {
                        schedule.beginPeriodLater(adapterPosition)
                        notifyItemRangeChanged(adapterPosition-1, 2)
                    }
                }

                override fun onBeginningExpanded() {
                    schedule.beginPeriodEarlier(adapterPosition)
                    notifyItemRangeChanged(adapterPosition-1, 2)
                }

                override fun onEndingCollapsed() {
                    safeCollapse {
                        schedule.finishPeriodEarlier(adapterPosition)
                        notifyItemRangeChanged(adapterPosition, 2)
                    }
                }

                override fun onEndingExpanded() {
                    schedule.finishPeriodLater(adapterPosition)
                    notifyItemRangeChanged(adapterPosition, 2)
                }


                private fun safeCollapse(collapse: () -> Unit) {
                    if(adapterPosition !in schedule.periods.indices) {
                        return
                    }

                    val period = schedule.periods[adapterPosition]
                    if(period.startDate.before(period.endDate)) {
                        collapse()
                    }
                    // Otherwise collapsing this SchedulePeriod will destroy it,
                    // which is not supported. There is a standalone mechanism
                    // for the user to delete SchedulePeriods.
                }
            } )
        }
    }


    override fun getItemCount(): Int
        = schedule.periods.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodViewHolder {
        val view = SchedulePeriodView(parent.context).apply {
            layoutParams = createLayoutParams()
        }
        return PeriodViewHolder(view)
    }

    override fun onBindViewHolder(holder: PeriodViewHolder, position: Int) {
        holder.bind( schedule.periods[position] )
    }


    private fun createLayoutParams(): ViewGroup.LayoutParams {
        return RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }
}