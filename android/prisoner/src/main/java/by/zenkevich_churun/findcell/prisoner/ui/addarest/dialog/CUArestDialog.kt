package by.zenkevich_churun.findcell.prisoner.ui.addarest.dialog

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import by.zenkevich_churun.findcell.core.util.party3.CalendarPickerUtil
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.addarest.vm.CUArestViewModel
import com.savvi.rangedatepicker.CalendarPickerView
import kotlinx.android.synthetic.main.add_arest_dialog.view.*
import kotlinx.android.synthetic.main.addarest_calpicker_view.*
import kotlinx.android.synthetic.main.addarest_calpicker_view.view.*
import java.text.SimpleDateFormat
import java.util.*


class CUArestDialog: DialogFragment() {

    private lateinit var vm: CUArestViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.add_arest_dialog, container, false)
        initCalendar(v)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields()

        view.buCancel.setOnClickListener {
            dismiss()
        }

        view.buSubmit.setOnClickListener {
            if(addArest()) {
                dismiss()
            }
        }
    }


    private fun initCalendar(v: View) {
        v.calpicker.init(
            date(1994, Calendar.JULY, 10),
            date(),
            SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        ).inMode( CalendarPickerView.SelectionMode.RANGE )
        v.calpicker.scrollToDate( date(2020, Calendar.AUGUST, 9) )
    }

    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = CUArestViewModel.get(appContext, this)
    }


    private fun addArest(): Boolean {
        val range = CalendarPickerUtil.selectedRange(calpicker) ?: return false
        vm.addArest(range.first, range.second)
        return true
    }


    private fun date(): Date {
        cal.timeInMillis = System.currentTimeMillis()
        return cal.time
    }

    private fun date(year: Int, month: Int, date: Int): Date {
        cal.set(year, month, date)
        return cal.time
    }


    companion object {
        private val cal by lazy { Calendar.getInstance() }
    }
}