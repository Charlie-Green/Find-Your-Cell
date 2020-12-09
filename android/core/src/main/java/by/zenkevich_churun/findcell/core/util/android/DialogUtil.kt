package by.zenkevich_churun.findcell.core.util.android

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager


object DialogUtil {

    fun removeBackground(target: Dialog) {
        val transparentDrawable = ColorDrawable(Color.TRANSPARENT)
        target.window?.setBackgroundDrawable(transparentDrawable)
    }


    fun showAt(target: Dialog, x: Int, y: Int) {
        val win = target.window

        win?.setGravity(Gravity.TOP or Gravity.START)
        win?.attributes = win?.attributes?.apply {
            this.x = x
            this.y = y
        }

        target.show()
    }


    fun setWidth(target: Dialog, w: Int) {
        val params = target.window?.attributes
            ?: WindowManager.LayoutParams(w, WRAP_CONTENT)
        params.width = w

        target.window?.attributes = params
    }
}