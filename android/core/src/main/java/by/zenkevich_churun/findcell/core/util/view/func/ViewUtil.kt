package by.zenkevich_churun.findcell.core.util.view.func

import android.view.ViewGroup


object ViewUtil {

    fun positionAt(parent: ViewGroup, y: Float): Int {
        var start = 0
        var end = parent.childCount
        if(end == 0) {
            return -1
        }

        while(end > start + 1) {
            val position = (start + end)/2
            val actualY = parent.getChildAt(position).y

            when {
                actualY < y -> start = position
                actualY > y -> end = position
                else        -> return position
            }
        }

        return start
    }
}