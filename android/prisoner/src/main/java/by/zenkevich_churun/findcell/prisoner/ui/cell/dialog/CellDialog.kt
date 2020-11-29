package by.zenkevich_churun.findcell.prisoner.ui.cell.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.cell.model.CellEditorState
import by.zenkevich_churun.findcell.prisoner.ui.cell.model.JailHeader
import kotlinx.android.synthetic.main.cell_dialog.*


class CellDialog: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activitySize = AndroidUtil.activitySize(requireActivity())

        return inflater.inflate(R.layout.cell_dialog, container, false).apply {
            minimumWidth = (activitySize.width*4)/5
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val state = CellEditorState(
            listOf(
                JailHeader(1, "Окрестина ИВС", 24),
                JailHeader(2, "Окрестина ЦИП", 40),
                JailHeader(3, "Жодино", 210),
                JailHeader(4, "Барановичи", 150),
                JailHeader(5, "Могилёв", 120)
            ),
            2,
            7,
            true
        )

        displayState(state)
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
}