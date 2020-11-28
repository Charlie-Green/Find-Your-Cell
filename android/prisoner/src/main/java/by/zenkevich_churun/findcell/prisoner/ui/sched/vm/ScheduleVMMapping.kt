package by.zenkevich_churun.findcell.prisoner.ui.sched.vm

import android.content.Context
import by.zenkevich_churun.findcell.prisoner.R


internal class ScheduleVMMapping(
    private val appContext: Context ) {

    val getFailedMessage: String
        get() = appContext.getString(R.string.get_schedule_failed_msg)

    val updateFailedMessage: String
        get() = appContext.getString(R.string.update_schedule_failed_msg)
}