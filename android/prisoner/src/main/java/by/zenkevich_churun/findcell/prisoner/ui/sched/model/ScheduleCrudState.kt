package by.zenkevich_churun.findcell.prisoner.ui.sched.model

import by.zenkevich_churun.findcell.entity.entity.Schedule
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.ScheduleLiveDatasStorage


enum class ScheduleCrudState(
    var notified: Boolean = false) {

    IDLE,
    GET_REQUIRES_INTERNET,
    LOADING,
    GET_FAILED,
    UPDATE_FAILED,

    /** In this case, the loaded [Schedule]
      * is obtained via [ScheduleLiveDatasStorage.scheduleLD]. **/
    GOT,

    UPDATED
}