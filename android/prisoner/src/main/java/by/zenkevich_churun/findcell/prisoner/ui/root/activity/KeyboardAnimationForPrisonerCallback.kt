package by.zenkevich_churun.findcell.prisoner.ui.root.activity

import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.core.view.updateLayoutParams
import by.zenkevich_churun.findcell.core.util.android.KeyboardAnimationCallback
import kotlin.math.max


@RequiresApi(30)
internal class KeyboardAnimationForPrisonerCallback(
    contentView: View,
    private val persistentBottomMargin: Int
): KeyboardAnimationCallback(contentView) {

    override fun onKeyboardHeightChanged(h: Int) {
        val newBottomMargin = max(h, persistentBottomMargin)
        setBottomMargin(animatedView, newBottomMargin)
    }


    companion object {

        fun setTo(window: Window, target: View) {
            window.setDecorFitsSystemWindows(false)

            target.setOnApplyWindowInsetsListener { _, insets ->
                // Get navigation bar height and align the View:
                val systemBarsInsets = insets.getInsets(WindowInsets.Type.systemBars())
                val navBarHeight = systemBarsInsets.bottom
                setBottomMargin(target, navBarHeight)

                // Set the callback:
                val callback = KeyboardAnimationForPrisonerCallback(target, navBarHeight)
                target.setWindowInsetsAnimationCallback(callback)

                /* return */ insets
            }
        }


        private fun setBottomMargin(target: View, value: Int) {
            target.updateLayoutParams<FrameLayout.LayoutParams> {
                bottomMargin = value
            }
        }
    }
}