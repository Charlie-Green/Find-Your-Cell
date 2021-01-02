package by.zenkevich_churun.findcell.prisoner.ui.addarest.dialog

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.core.util.android.DialogUtil
import by.zenkevich_churun.findcell.prisoner.R
import com.savvi.rangedatepicker.CalendarPickerView
import kotlinx.android.synthetic.main.add_arest_dialog.*
import kotlinx.android.synthetic.main.add_arest_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*


class AddArestDialog: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setDimensions()
        val v = inflater.inflate(R.layout.add_arest_dialog, container, false)
        v.calpicker.init(
            Calendar.getInstance().apply { set(1994, Calendar.JULY, 1) }.time,
            Calendar.getInstance().time,
            SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        ).inMode( CalendarPickerView.SelectionMode.RANGE )

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // ...
    }


    private fun setDimensions() {
        val activitySize = activity?.let { AndroidUtil.activitySize(it) } ?: return

        val w = (activitySize.width*4)/5
        val h = (activitySize.height*4)/5
        dialog?.also { DialogUtil.setDimensions(it, w, h) }
    }
}