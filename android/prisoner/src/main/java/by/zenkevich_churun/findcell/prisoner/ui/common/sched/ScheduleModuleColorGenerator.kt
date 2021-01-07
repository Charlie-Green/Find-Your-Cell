package by.zenkevich_churun.findcell.prisoner.ui.common.sched

import java.util.*


internal class ScheduleModuleColorGenerator {
    private val colors = listOf(
        0xff_f00000.toInt(),
        0xff_0000f0.toInt(),
        0xff_009000.toInt(),
        0xff_804000.toInt(),
        0xff_004080.toInt()
    )
    private var colorIndex = 0

    val next: Int
        get() {
            synchronized(colors) {
                return colors[ (colorIndex++) % colors.size ]
            }
        }

    fun reset() {
        colorIndex = 0
    }
}