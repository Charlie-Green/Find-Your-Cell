package by.zenkevich_churun.findcell.prisoner.ui.celledit.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.updateLayoutParams
import by.zenkevich_churun.findcell.core.ui.common.SviazenDialog
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.databinding.CellEditDialogBinding
import by.zenkevich_churun.findcell.prisoner.ui.celledit.vm.CellEditorViewModel
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.ScheduleCellsCrudState


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
        val vm = CellEditorViewModel
            .get(appContext, this)
            .also { this.vm = it }

        vm.notifyUiShowing()

        vm.cellCrudStateLD.observe(viewLifecycleOwner, { state ->
            renderState(state)
        })

        vb.buSave.setOnClickListener {
            submitDraft()
            vm.save()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        submitDraft()
        if(activity?.isChangingConfigurations == false) {
            vm?.notifyUiDismissed()
        }

        super.onDismiss(dialog)
    }


    private fun resizeDialog() {
        val activitySize = AndroidUtil.activitySize(requireActivity())
        vb.vlltContent.updateLayoutParams {
            width = (activitySize.width*4)/5
        }
    }


    private fun renderState(state: ScheduleCellsCrudState) {
        if(state is ScheduleCellsCrudState.Processing) {
            dismiss()
            return
        }

        if(state !is ScheduleCellsCrudState.Editing) {
            return
        }

        renderEditor(state)
        when(state) {
            is ScheduleCellsCrudState.Editing.AddFailed -> {
                notifyError(R.string.add_cell_failed_msg)
            }

            is ScheduleCellsCrudState.Editing.UpdateFailed -> {
                notifyError(R.string.update_cell_failed_msg)
            }
        }
    }

    private fun renderEditor(state: ScheduleCellsCrudState.Editing) {
        vb.txtvError.visibility = View.GONE

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

        vb.buSave.setText(
            if(state is ScheduleCellsCrudState.Editing.Adding) R.string.add
            else R.string.save )
    }

    private fun notifyError(messageRes: Int) {
        vb.txtvError.visibility = View.VISIBLE
        vb.txtvError.setText(messageRes)
    }


    private fun submitDraft() {
        vm?.submitEditorState(
            vb.spJail.selectedItemPosition,
            vb.numpCellNumber.value.toShort()
        )
    }
}