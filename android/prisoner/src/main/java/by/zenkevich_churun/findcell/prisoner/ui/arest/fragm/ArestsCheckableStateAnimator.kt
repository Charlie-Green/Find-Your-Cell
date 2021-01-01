package by.zenkevich_churun.findcell.prisoner.ui.arest.fragm

import android.animation.ValueAnimator
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import by.zenkevich_churun.findcell.prisoner.R
import java.util.*


internal class ArestsCheckableStateAnimator private constructor(
    private val contentViews: Collection<ContentViewInfo>,
    private val screenHeight: Int,
    private val topButton:    View,
    private val bottomButton: View ) {

    private val marginAboveButtons: Int
    private val marginBetweenButtons: Int
    private var stateResolved = false
    private var checkable = false
    private var tbh = 0  // Top button height. Initialized later.
    private var bbh = 0  // Bottom button height. Initialized later.


    init {
        val res = topButton.context.resources
        marginAboveButtons = res.getDimensionPixelSize(R.dimen.arest_deletebutton_margin_top)
        marginBetweenButtons = res.getDimensionPixelSize(R.dimen.arest_deletebutton_margin_bottom)
    }


    fun setCheckable(isCheckable: Boolean) {
        if(stateResolved && checkable == isCheckable) {
            return
        }
        checkable = isCheckable


        if(stateResolved) {
            // State has been resolved and now it's changed. Animate.
            animateToState(marginWhenCheckable)
        } else {
            // State is being first initialized. Don't animate
            tbh = measureButton(topButton)
            bbh = measureButton(bottomButton)
            Log.v("CharlieDebug", "Set $marginWhenCheckable")
            setState(marginWhenCheckable)
        }

        stateResolved = false
    }


    private val marginWhenCheckable: Int
        get() = tbh + bbh + marginAboveButtons + marginBetweenButtons

    private val currentMargin: Int
        get() {
            if(contentViews.isEmpty()) {
                return 0
            }

            val info = contentViews.first()
            return info.view.marginBottom - info.persistentMargin
        }

    private fun measureButton(b: View): Int {
        if(b.height != 0) {
            return topButton.height
        }

        b.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        return b.measuredHeight
    }


    private fun animateToState(desiredMargin: Int) {
        Log.v("CharlieDebug", "Animate to $desiredMargin")

        ValueAnimator.ofInt(currentMargin, desiredMargin)
            .setDuration(2000L)
            .apply { addUpdateListener { animer ->
                setState(animer.animatedValue as Int)
            }}.start()
    }

    private fun setState(contentMargin: Int) {
        for(info in contentViews) {
            info.view.updateLayoutParams<FrameLayout.LayoutParams> {
                bottomMargin = contentMargin + info.persistentMargin
            }
        }

        val topTranslation = (screenHeight - contentMargin).toFloat()
        topButton.translationY = topTranslation
        bottomButton.translationY = topTranslation + tbh + marginBetweenButtons
    }


    class Builder(
        private val screenHeight: Int,
        private val topButton: View,
        private val bottomButton: View ) {

        private val contentViews = LinkedList<ContentViewInfo>()

        fun addContentView(
            view: View,
            persistentBottomMargin: Int
        ): ArestsCheckableStateAnimator.Builder {

            val info = ContentViewInfo(view, persistentBottomMargin)
            contentViews.add(info)
            return this
        }

        fun build(): ArestsCheckableStateAnimator {
            return ArestsCheckableStateAnimator(
                contentViews,
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