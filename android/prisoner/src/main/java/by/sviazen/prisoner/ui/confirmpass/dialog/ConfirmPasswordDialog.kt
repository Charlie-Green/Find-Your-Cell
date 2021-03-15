package by.sviazen.prisoner.ui.confirmpass.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import by.sviazen.core.ui.common.SviazenDialog
import by.sviazen.prisoner.databinding.ConfirmPasswordDialogBinding
import by.sviazen.prisoner.ui.confirmpass.vm.ConfirmPasswordViewModel


class ConfirmPasswordDialog: SviazenDialog<ConfirmPasswordDialogBinding>() {

    private lateinit var vm: ConfirmPasswordViewModel


    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = ConfirmPasswordDialogBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields()

        vb.txtvSubmit.setOnClickListener {
            handleSubmit()
        }
    }


    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = ConfirmPasswordViewModel.get(appContext, this)
    }

    private fun handleSubmit() {
        val password = vb.tietPassword.text.toString()
        val validated = vm.submit(password)

        if(validated) {
            dismiss()
        } else {
            vb.txtvError.visibility = View.VISIBLE
        }
    }
}