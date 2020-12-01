package by.zenkevich_churun.findcell.prisoner.ui.cell.dialog

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
import by.zenkevich_churun.findcell.prisoner.ui.cell.vm.CellViewModel
import kotlinx.android.synthetic.main.cell_dialog.*
import kotlinx.android.synthetic.main.cell_dialog.view.*


class CellDialog: DialogFragment() {
    private var vm: CellViewModel? = null


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
        val vm = CellViewModel.get(appContext, this).also { this.vm = it }

        val args = requireArguments()
        vm.requestState(
            CellDialogArguments.jailId(args),
            CellDialogArguments.cellNumber(args)
        )

        vm.editorStateLD.observe(viewLifecycleOwner, Observer { state ->
            displayState(state)
        })
        vm.loadingLD.observe(viewLifecycleOwner, Observer { loading ->
            prBar.isVisible = loading
        })
        vm.errorLD.observe(viewLifecycleOwner, Observer { message ->
            notifyError(message)
        })
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


    companion object {
        fun arguments(jailId: Int, cellNumber: Short): Bundle
            = CellDialogArguments.create(jailId, cellNumber)
    }
}