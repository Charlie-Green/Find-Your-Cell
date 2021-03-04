package by.sviazen.prisoner.ui.arest.fragm

import android.animation.ValueAnimator
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import by.sviazen.prisoner.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


internal class ArestsCheckableStateAnimator(
    private val resizableView: RecyclerView,
    private val fadingView: FloatingActionButton,
    private val topButton: Button,
    private val bottomButton: Button ) {

    private val marginAboveButtons: Int
    private val marginBetweenButtons: Int
    private val buttonHeight: Int
    private var stateResolved = false
    private var checkable = false


    init {
        val res = resizableView.context.resources
        marginAboveButtons   = res.getDimensionPixelSize(R.dimen.arest_deletebutton_margin_top)
        marginBetweenButtons = res.getDimensionPixelSize(R.dimen.arest_deletebutton_margin_bottom)
        buttonHeight         = res.getDimensionPixelSize(R.dimen.arest_deletebutton_height)
    }


    fun setCheckable(isCheckable: Boolean) {
        if(stateResolved && checkable == isCheckable) {
            return
        }
        checkable = isCheckable

        val desiredMargin = if(isCheckable) marginWhenCheckable else 0
        val desiredAlpha  = if(isCheckable) 0f else 1f

        if(stateResolved) {
            // State has been resolved and now it's changed. Animate.
            animateToState(desiredMargin, desiredAlpha)
        } else {
            // State is being first initialized. Don't animate
            setState(desiredMargin, desiredAlpha)

            stateResolved = true
        }
    }


    private val marginWhenCheckable: Int
        get() = 2*buttonHeight + marginAboveButtons + marginBetweenButtons

    private val currentMargin: Int
        get() {
            return resizableView.marginBottom
        }


    private fun animateToState(desiredMargin: Int, desiredAlpha: Float) {
        val res = resizableView.context.resources
        val initialAlpha = fadingView.alpha
        val duration = res.getInteger(R.integer.arest_delete_animation_duration).toLong()

        ValueAnimator.ofInt(currentMargin, desiredMargin)
            .setDuration(duration)
            .apply { addUpdateListener { animer ->
                val margin = animer.animatedValue as Int
                val alpha = initialAlpha + animer.animatedFraction*(desiredAlpha-initialAlpha)
                setState(margin, alpha)
            }}.start()
    }

    private fun setState(contentMargin: Int, alpha: Float) {
        // Set alpha:
        fadingView.alpha = alpha

        // Resize the resisable view:
        resizableView.updateLayoutParams<LinearLayout.LayoutParams> {
            bottomMargin = contentMargin
        }

        // Position the buttons:
        val parent = resizableView.parent as ViewGroup
        for(j in 0 until parent.childCount) {
            val child = parent.getChildAt(j)
            if(child !== resizableView) {
                child.translationY = -1f * contentMargin
            }
        }
    }
}