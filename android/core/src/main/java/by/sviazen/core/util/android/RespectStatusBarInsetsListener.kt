package by.sviazen.core.util.android

import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.view.updateLayoutParams


/** [View.OnApplyWindowInsetsListener] to adjust top margin of the given [Toolbar]
  * so that system status bar doesn't overlap it. **/
@RequiresApi(30)
class RespectStatusBarInsetsListener(
    private val toolbar: Toolbar
): View.OnApplyWindowInsetsListener {

    override fun onApplyWindowInsets(
        v: View,
        insets: WindowInsets
    ): WindowInsets {

        val statusInsets = insets.getInsets(WindowInsets.Type.statusBars())
        val statusHeight = statusInsets.top - statusInsets.bottom

        toolbar.updateLayoutParams<LinearLayout.LayoutParams> {
            topMargin = statusHeight
        }

        return insets
    }
}