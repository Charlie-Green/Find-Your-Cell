package by.zenkevich_churun.findcell.prisoner.ui.interrupt.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.interrupt.vm.EditInterruptViewModel
import kotlinx.android.synthetic.main.result_request_dialog.*


class EditInterruptDialog: DialogFragment() {

    private lateinit var vm: EditInterruptViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.result_request_dialog, container, false).also { view ->
            setDialogWidth(view)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields()

        buNo.setOnClickListener {
            dismiss()
        }
        buYes.setOnClickListener {
            // TODO: Navigate to results.
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        vm.notifyInterruptDeclined()
        super.onDismiss(dialog)
    }


    private fun setDialogWidth(view: View) {
        val screenSize = AndroidUtil.activitySize(requireActivity())
        val desiredWidth = (screenSize.width*4)/5
        view.minimumWidth = desiredWidth
    }

    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = EditInterruptViewModel.get(appContext, this)
    }
}