package by.sviazen.core.util.android

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import androidx.core.util.Pair
import androidx.fragment.app.FragmentManager
import by.sviazen.core.R
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*


object DialogUtil {

    private const val TAG_DATE_RANGE_PICKER_DIALOG = "DateRange"


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
        updateParams(target) {
            width = w
        }
    }

    fun setDimensions(target: Dialog, w: Int, h: Int) {
        updateParams(target) {
            width = w
            height = h
        }
    }


    inline fun updateParams(target: Dialog, update: WindowManager.LayoutParams.() -> Unit) {
        val params = target.window?.attributes
            ?: WindowManager.LayoutParams()
        params.update()
        target.window?.attributes = params
    }
}