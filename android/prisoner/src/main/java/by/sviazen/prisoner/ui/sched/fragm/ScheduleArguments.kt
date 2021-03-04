package by.sviazen.prisoner.ui.sched.fragm

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment


internal class ScheduleArguments private constructor(
    val arestId: Int ) {

    companion object {

        private const val KEY_AREST_ID = "arest"


        fun createBundle(
            arestId: Int
        ) = bundleOf(KEY_AREST_ID to arestId)

        fun of(target: Fragment): ScheduleArguments {
            val bundle = target.requireArguments()
            return ScheduleArguments(
                bundle.getInt(KEY_AREST_ID)
            )
        }
    }
}