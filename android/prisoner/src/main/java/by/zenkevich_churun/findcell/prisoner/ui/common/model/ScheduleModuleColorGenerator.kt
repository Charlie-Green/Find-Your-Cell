package by.zenkevich_churun.findcell.prisoner.ui.common.model

import java.util.*


internal class ScheduleModuleColorGenerator {
    private val colors = LinkedList<Int>().apply {
        add(0xff_f00000.toInt())
        add(0xff_0000f0.toInt())
        add(0xff_009000.toInt())
        add(0xff_00f0f0.toInt())
        add(0xff_f0f000.toInt())
        add(0xff_f000f0.toInt())
        add(0xff_804000.toInt())
        add(0xff_004080.toInt())
    }

    val next: Int
        get() {
            synchronized(colors) {
                val color = colors.pollFirst()!!
                colors.add(color)
                return color
            }
        }
}