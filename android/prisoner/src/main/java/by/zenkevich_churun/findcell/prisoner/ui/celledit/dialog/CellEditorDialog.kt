package by.zenkevich_churun.findcell.prisoner.ui.celledit.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Observer
import by.zenkevich_churun.findcell.core.ui.common.SviazenDialog
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.databinding.CellEditDialogBinding
import by.zenkevich_churun.findcell.prisoner.ui.celledit.model.CellEditorState
import by.zenkevich_churun.findcell.prisoner.ui.celledit.vm.CellEditorViewModel


class CellEditorDialog: SviazenDialog<CellEditDialogBinding>() {
    private var vm: CellEditorViewModel? = null


    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = CellEditDialogBinding.inflate(inflater)

    override fun customizeDialog(view: View) {
        resizeDialog()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val appContext = view.context.applicationContext
        val vm = CellEditorViewModel.get(appContext, this).also { this.vm = it }

        val args = CellEditorDialogArguments.from(this)
        vm.requestState(args.jailId, args.cellNumber)

        vm.editorStateLD.observe(viewLifecycleOwner, Observer { state ->
            state?.also { displayState(it) }
        })
        vm.loadingLD.observe(viewLifecycleOwner, Observer { loading ->
            vb.prBar.isVisible = loading
        })
        vm.errorLD.observe(viewLifecycleOwner, Observer { message ->
            notifyError(message)
        })
        vm.cellUpdateLD.observe(viewLifecycleOwner, Observer { update ->
            if(update != null) {
                dismiss()
            }
        })

        vb.buSave.setOnClickListener {
            submitDraft()
            vm.save()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        vm?.notifyUiDismissed()
    }


    private fun resizeDialog() {
        val activitySize = AndroidUtil.activitySize(requireActivity())
        vb.vlltContent.updateLayoutParams {
            width = (activitySize.width*4)/5
        }
    }


    private fun displayState(state: CellEditorState) {
        vb.spJail.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            state.jails
        )
        vb.spJail.setSelection(state.jailIndex)

        vb.numpCellNumber.apply {
            minValue = 1
            maxValue = state.jails[state.jailIndex].cellCount.toInt()
            value = state.cellNumber.toInt()
        }

        vb.spJail.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long ) {

                vb.numpCellNumber.maxValue = state.jails[position].cellCount.toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {   }
        }

        vb.buSave.setText( if(state.isNew) R.string.add else R.string.save )
    }

    private fun notifyError(message: String?) {
        if(message == null) {
            vb.txtvError.visibility = View.GONE
        } else {
            vb.txtvError.visibility = View.VISIBLE
            vb.txtvError.text = message
        }
    }


    private fun submitDraft() {
        val vm = this.vm ?: return
        val state = vm.editorStateLD?.value ?: return

        val draft = CellEditorState(
            state.jails,
            state.oldJailIndex,
            vb.spJail.selectedItemPosition,
            state.oldCellNumber,
            vb.numpCellNumber.value.toShort()
        )

        vm.submitState(draft)
    }


    companion object {
        fun arguments(jailId: Int, cellNumber: Short): Bundle
            = CellEditorDialogArguments.create(jailId, cellNumber)
    }
}