package by.zenkevich_churun.findcell.prisoner.ui.cell.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.cell.model.CellEditorState
import by.zenkevich_churun.findcell.prisoner.ui.cell.vm.CellEditorViewModel
import kotlinx.android.synthetic.main.cell_dialog.*
import kotlinx.android.synthetic.main.cell_dialog.view.*


class CellEditorDialog: DialogFragment() {
    private var vm: CellEditorViewModel? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activitySize = AndroidUtil.activitySize(requireActivity())

        return inflater.inflate(R.layout.cell_dialog, container, false).apply {
            vlltContent.updateLayoutParams {
                width = (activitySize.width*4)/5
            }
        }
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
            prBar.isVisible = loading
        })
        vm.errorLD.observe(viewLifecycleOwner, Observer { message ->
            notifyError(message)
        })
        vm.cellUpdateLD.observe(viewLifecycleOwner, Observer { update ->
            if(update != null) {
                dismiss()
            }
        })

        buSave.setOnClickListener {
            submitDraft()
            vm.save()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        vm?.notifyUiDismissed()
    }


    private fun displayState(state: CellEditorState) {
        spJail.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            state.jails
        )
        spJail.setSelection(state.jailIndex)

        numpCellNumber.apply {
            minValue = 1
            maxValue = state.jails[state.jailIndex].cellCount.toInt()
            value = state.cellNumber.toInt()
        }

        spJail.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long ) {

                numpCellNumber.maxValue = state.jails[position].cellCount.toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {   }
        }

        buSave.setText( if(state.isNew) R.string.add else R.string.save )
    }

    private fun notifyError(message: String?) {
        if(message == null) {
            txtvError.visibility = View.GONE
        } else {
            txtvError.visibility = View.VISIBLE
            txtvError.text = message
        }
    }


    private fun submitDraft() {
        val vm = this.vm ?: return
        val state = vm.editorStateLD?.value ?: return

        val draft = CellEditorState(
            state.jails,
            state.oldJailIndex,
            spJail.selectedItemPosition,
            state.oldCellNumber,
            numpCellNumber.value.toShort(),
            state.isNew
        )

        vm.submitState(draft)
    }


    companion object {
        fun arguments(jailId: Int, cellNumber: Short): Bundle
            = CellEditorDialogArguments.create(jailId, cellNumber)
    }
}