package by.zenkevich_churun.findcell.core.util.android

import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi


/** [WindowInsetsAnimation.Callback] to animate content
  * according to the system keyboard's enter and exit animations. **/
@RequiresApi(30)
abstract class KeyboardAnimationCallback:
WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {

    override fun onProgress(
        insets: WindowInsets,
        runningAnimations: MutableList<WindowInsetsAnimation>
    ): WindowInsets {

        val keyboadType = WindowInsets.Type.ime()
        val keyboardAnimation = runningAnimations.find { anim ->
            anim.typeMask == keyboadType
        }

        if(keyboardAnimation != null) {
            val keyboardInsets = insets.getInsets(keyboadType)
            val keyboardHeight = keyboardInsets.bottom - keyboardInsets.top
            Log.v("CharlieDebug", "h = $keyboardHeight")
            onKeyboardHeightChanged(keyboardHeight)
        } else {
            Log.v("CharlieDebug", "No keyboard animation")
        }

        return insets
    }


    protected abstract fun onKeyboardHeightChanged(h: Int)
}