package by.zenkevich_churun.findcell.prisoner.util.view.period

import by.zenkevich_churun.findcell.core.entity.sched.SchedulePeriod


/** Listener to be invoked when the user expands or collapses a [SchedulePeriod]. **/
interface SchedulePeriodResizedListener {
    /** Beginning of this [SchedulePeriod] was put 1 day ealier. **/
    fun onBeginningExpanded()

    /** Beginning of this [SchedulePeriod] was put 1 day later. **/
    fun onBeginningCollapsed()

    /** End of this [SchedulePeriod] was put 1 day later. **/
    fun onEndingExpanded()

    /** End of this [SchedulePeriod] was put 1 day earlier. **/
    fun onEndingCollapsed()
}