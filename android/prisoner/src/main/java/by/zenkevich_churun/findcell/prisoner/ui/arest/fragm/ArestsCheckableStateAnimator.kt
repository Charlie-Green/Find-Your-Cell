package by.zenkevich_churun.findcell.prisoner.ui.arest.fragm

import android.animation.ValueAnimator
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import by.zenkevich_churun.findcell.prisoner.R


internal class ArestsCheckableStateAnimator private constructor(
    private val contentInfo: ContentViewInfo,
    private val fadingView: View?,
    private val screenHeight: Int,
    private val topButton:    View,
    private val bottomButton: View ) {

    private val marginAboveButtons: Int
    private val marginBetweenButtons: Int
    private val buttonHeight: Int
    private var stateResolved = false
    private var checkable = false


    init {
        val res = topButton.context.resources
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
            return contentInfo.view.marginBottom - contentInfo.persistentMargin
        }


    private fun animateToState(desiredMargin: Int, desiredAlpha: Float) {
        val res = topButton.context.resources
        val initialAlpha = fadingView?.alpha ?: 0f
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
        contentInfo.view.updateLayoutParams<FrameLayout.LayoutParams> {
            bottomMargin = contentMargin + contentInfo.persistentMargin
        }
        fadingView?.alpha = alpha

        val topTranslation = (screenHeight - contentMargin).toFloat()
        topButton.translationY = topTranslation
        bottomButton.translationY = topTranslation + buttonHeight + marginBetweenButtons
    }


    class Builder(
        private val screenHeight: Int,
        private val topButton: View,
        private val bottomButton: View ) {

        private var contentInfo: ArestsCheckableStateAnimator.ContentViewInfo? = null
        private var fadingView: View? = null


        fun setContentView(
            view: View,
            persistentBottomMargin: Int
        ): ArestsCheckableStateAnimator.Builder {

            contentInfo = ContentViewInfo(view, persistentBottomMargin)
            return this
        }

        fun setFadingView(view: View): ArestsCheckableStateAnimator.Builder {
            fadingView = view
            return this
        }

        fun build(): ArestsCheckableStateAnimator {
            return ArestsCheckableStateAnimator(
                contentInfo ?: throw IllegalStateException("Content view not set"),
                fadingView,
                screenHeight,
                topButton,
                bottomButton
            )
        }
    }


    private class ContentViewInfo(
        val view: View,
        val persistentMargin: Int
    )
}