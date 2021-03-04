package by.sviazen.prisoner.ui.common.sched.util


internal class ScheduleModelColorGenerator {
    private val colors = listOf(
        0xff_f00000.toInt(),
        0xff_0000f0.toInt(),
        0xff_009000.toInt(),
        0xff_804000.toInt(),
        0xff_004080.toInt()
    )
    private var colorIndex = 0

    val current: Int
        get() = colors[(colorIndex - 1) % colors.size]

    val next: Int
        get() {
            synchronized(colors) {
                return colors[ (colorIndex++) % colors.size ]
            }
        }

    val currentTextColor: Int
        get() {
            val backColor = current

            val rgb0 = backColor and 0xffffff.toInt()
            val rgb = 0xffffff - rgb0
            val alpha = backColor and 0xff_000000.toInt()

            return alpha or rgb
        }

    val currentNumberBackColor: Int
        get() {
            val backColor = current

            val r0 = (backColor and 0x00_ff0000.toInt()) shr 16
            val g0 = (backColor and 0x00_00ff00.toInt()) shr 8
            val b0 = backColor and 0x00_0000ff.toInt()

            val r = r0/2
            val g = g0/2
            val b = b0/2

            val alpha = backColor and 0xff_000000.toInt()
            return alpha or (r shl 16) or (g shl 8) or b
        }

    fun reset() {
        colorIndex = 0
    }
}