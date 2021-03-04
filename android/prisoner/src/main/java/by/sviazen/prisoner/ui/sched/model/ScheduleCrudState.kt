package by.sviazen.prisoner.ui.sched.model

import by.sviazen.prisoner.ui.common.sched.ld.ScheduleLiveDatasStorage


sealed class ScheduleCrudState{

    object Idle: ScheduleCrudState()
    object Loading: ScheduleCrudState()

    /** In this case, the loaded [Schedule]
     * is obtained via [ScheduleLiveDatasStorage.scheduleLD]. **/
    object Got: ScheduleCrudState()
    
    class GetFailed: ScheduleCrudState() {
        var notified = false
    }

    class UpdateFailed: ScheduleCrudState() {
        var notified = false
    }

    class Updated: ScheduleCrudState() {
        var notified = false
    }
}