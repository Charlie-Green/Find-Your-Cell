package by.zenkevich_churun.findcell.prisoner.ui.root.activity

import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.core.view.updateLayoutParams
import by.zenkevich_churun.findcell.core.util.android.KeyboardAnimationCallback


// TODO: CharlieDebug: either make it work or remove it
@RequiresApi(30)
internal class KeyboardAnimationForPrisonerCallback(
    private val contentView: View
): KeyboardAnimationCallback() {

    override fun onKeyboardHeightChanged(h: Int) {
        contentView.updateLayoutParams<FrameLayout.LayoutParams> {
            bottomMargin = h
        }
    }


    companion object {

        fun setTo(target: View) {
            val callback = KeyboardAnimationForPrisonerCallback(target)
            target.setWindowInsetsAnimationCallback(callback)
        }
    }
}