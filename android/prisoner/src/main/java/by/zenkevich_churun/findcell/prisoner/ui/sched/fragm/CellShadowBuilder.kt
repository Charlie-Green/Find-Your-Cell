package by.zenkevich_churun.findcell.prisoner.ui.sched.fragm

import android.graphics.Point
import android.view.View


internal class CellShadowBuilder(
    view: View
): View.DragShadowBuilder(view) {

    override fun onProvideShadowMetrics(size: Point, touch: Point) {
        super.onProvideShadowMetrics(size, touch)
        touch.x = (size.x*7)/8
    }
}