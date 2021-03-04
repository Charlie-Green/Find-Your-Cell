package by.sviazen.prisoner.ui.addarest.dialog

import android.os.Bundle
import android.view.*
import by.sviazen.core.ui.common.SviazenDialog
import by.sviazen.core.util.party3.CalendarPickerUtil
import by.sviazen.prisoner.databinding.AddArestDialogBinding
import by.sviazen.prisoner.ui.addarest.vm.CUArestViewModel
import com.savvi.rangedatepicker.CalendarPickerView
import java.text.SimpleDateFormat
import java.util.*


class CUArestDialog: SviazenDialog<AddArestDialogBinding>() {

    private lateinit var vm: CUArestViewModel


    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = AddArestDialogBinding.inflate(inflater)

    override fun customizeDialog(view: View) {
        initCalendar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields()

        vb.buCancel.setOnClickListener {
            dismiss()
        }

        vb.buSubmit.setOnClickListener {
            if(addArest()) {
                dismiss()
            }
        }
    }


    private fun initCalendar() {
        val picker = vb.calpicker.root

        picker.init(
            date(1994, Calendar.JULY, 10),
            date(),
            SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        ).inMode( CalendarPickerView.SelectionMode.RANGE )

        picker.scrollToDate( date(2020, Calendar.AUGUST, 9) )
    }

    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = CUArestViewModel.get(appContext, this)
    }


    private fun addArest(): Boolean {
        val range = CalendarPickerUtil.selectedRange(
            vb.calpicker.root
        ) ?: return false

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