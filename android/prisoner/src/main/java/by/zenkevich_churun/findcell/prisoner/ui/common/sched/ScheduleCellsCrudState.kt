package by.zenkevich_churun.findcell.prisoner.ui.common.sched

import by.zenkevich_churun.findcell.entity.entity.Cell
import by.zenkevich_churun.findcell.entity.entity.Schedule


/** CRUD states of [Cell]s within a [Schedule] **/
sealed class ScheduleCellsCrudState {

    object Idle: ScheduleCellsCrudState()
    object AddRequested: ScheduleCellsCrudState()
    object Processing: ScheduleCellsCrudState()
    object GetJailsNeedsInternet: ScheduleCellsCrudState()
    object GetJailsFailed: ScheduleCellsCrudState()

    sealed class Editing(
        val jails: List<JailHeader>,
        val jailIndex: Int,
        val cellNumber: Short
    ): ScheduleCellsCrudState() {

        class Adding(
            jails: List<JailHeader>,
            jailIndex: Int,
            cellNumber: Short
        ): ScheduleCellsCrudState.Editing(jails, jailIndex, cellNumber)

        class Updating(
            val original: Cell,
            jails: List<JailHeader>,
            jailIndex: Int,
            cellNumber: Short
        ): ScheduleCellsCrudState.Editing(jails, jailIndex, cellNumber)


        val selectedJail: JailHeader?
            get() {
                if(jailIndex !in jails.indices) {
                    return null
                }

                return jails[jailIndex]
            }
    }

    class ViewingOptions(
        val target: Cell
    ): ScheduleCellsCrudState()

    class UpdateRequested(
        val original: Cell
    ): ScheduleCellsCrudState()

    class ConfirmingDelete(
        val target: Cell
    ): ScheduleCellsCrudState()

    class AddFailed: ScheduleCellsCrudState() {
        var notified = false
    }

    class UpdateFailed: ScheduleCellsCrudState()  {
        var notified = false
    }

    class DeleteFailed: ScheduleCellsCrudState()  {
        var notified = false
    }

    class Added: ScheduleCellsCrudState() {
        var notified = false
    }

    class Updated: ScheduleCellsCrudState() {
        var notified = false
    }

    class Deleted: ScheduleCellsCrudState() {
        var notified = false
    }
}