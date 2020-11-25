package by.zenkevich_churun.findcell.prisoner.util.view.period

import android.view.MotionEvent
import android.view.View


/** [View.OnTouchListener] used to implement the mechanism
  * of invocation [SchedulePeriodResizedListener] callbacks. **/
internal class SchedulePeriodTouchListener(
    private val listener: SchedulePeriodResizedListener,

    /** Vertical distance user's finger must come through
      * for [SchedulePeriodResizedListener] to be invoked. **/
    heightDelta: Int
): View.OnTouchListener {

    private enum class State {
        IDLE,
        CHANGE_BEGINNING,
        CHANGE_ENDING
    }


    private val dh = heightDelta.toFloat()
    private var state = State.IDLE
    private var lastY = 0f


    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return when(event.actionMasked) {
            MotionEvent.ACTION_DOWN                          -> handleDown(v, event)
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> handleCancel(v, event)
            else                                             -> handleMove(v, event)
        }
    }


    private fun handleDown(v: View, event: MotionEvent): Boolean {
        state = when {
            event.y <= 0.4f*v.height -> State.CHANGE_BEGINNING
            event.y >= 0.6f*v.height -> State.CHANGE_ENDING
            else -> return false
        }

        lastY = event.y
        return true
    }

    private fun handleMove(v: View, event: MotionEvent): Boolean {
        if(state == State.IDLE) {
            return false
        }

        val affectsEnding = (state == State.CHANGE_ENDING)
        if(event.y <= lastY - dh) {
            // The period moved 1 day up.
            if(affectsEnding) {
                listener.onEndingCollapsed()
            } else {
                listener.onBeginningExpanded()
            }

            lastY = event.y
        } else if(event.y >= lastY + dh) {
            // The period moved 1 day down.
            if(affectsEnding) {
                listener.onEndingExpanded()
            } else {
                listener.onBeginningCollapsed()
            }

            lastY = event.y
        }

        return true
    }

    private fun handleCancel(v: View, event: MotionEvent): Boolean {
        state = State.IDLE
        return false
    }
}