package by.zenkevich_churun.findcell.prisoner.ui.interrupt.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import by.zenkevich_churun.findcell.core.ui.common.SviazenDialog
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.prisoner.databinding.EditInterruptDialogBinding
import by.zenkevich_churun.findcell.prisoner.ui.interrupt.vm.EditInterruptViewModel


class EditInterruptDialog: SviazenDialog<EditInterruptDialogBinding>() {

    private lateinit var vm: EditInterruptViewModel
    private var confirmed = false


    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = EditInterruptDialogBinding.inflate(inflater)

    override fun customizeDialog(view: View) {
        setDialogWidth(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields()

        vb.buNo.setOnClickListener {
            confirmed = false
            dismiss()
        }
        vb.buYes.setOnClickListener {
            confirmed = true
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        if(confirmed) {
            vm.notifyInterruptConfirmed()
        } else {
            vm.notifyInterruptDeclined()
        }

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

        confirmed = false
    }
}