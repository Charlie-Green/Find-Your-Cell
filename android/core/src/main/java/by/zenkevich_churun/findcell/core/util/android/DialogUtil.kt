package by.zenkevich_churun.findcell.core.util.android

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import androidx.core.util.Pair
import androidx.fragment.app.FragmentManager
import by.zenkevich_churun.findcell.core.R
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
        val params = target.window?.attributes
            ?: WindowManager.LayoutParams(w, WRAP_CONTENT)
        params.width = w

        target.window?.attributes = params
    }


    fun pickDateRange(
        fragmentMan: FragmentManager,
        initialRange: Pair<Long, Long>?,
        onSelected: (start: Long, end: Long) -> Unit ) {

        val pickerBuilder = MaterialDatePicker
            .Builder
            .dateRangePicker()
            .setTheme(R.style.datepicker_dialog_theme)
        initialRange?.also { pickerBuilder.setSelection(it) }
        val picker = pickerBuilder.build()
        picker.show(fragmentMan, TAG_DATE_RANGE_PICKER_DIALOG)

        picker.addOnPositiveButtonClickListener { range ->
            onSelected(range.first!!, range.second!!)
        }
    }

    fun pickDateRange(
        fragmentMan: FragmentManager,
        initialStart: Calendar,
        initialEnd: Calendar,
        onSelected: (start: Long, end: Long) -> Unit

    ) = pickDateRange(
        fragmentMan,
        Pair(initialStart.timeInMillis, initialEnd.timeInMillis),
        onSelected
    )

    fun restoreDateRangePicker(
        fragmentMan: FragmentManager,
        onSelected: (start: Long, end: Long) -> Unit ) {

        val picker = fragmentMan
            .findFragmentByTag(TAG_DATE_RANGE_PICKER_DIALOG)
            as? MaterialDatePicker< Pair<Long, Long> >
            ?: return

        picker.addOnPositiveButtonClickListener { range ->
            onSelected(range.first!!, range.second!!)
        }
    }
}