package by.zenkevich_churun.findcell.prisoner.ui.cellopt.dialog

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import by.zenkevich_churun.findcell.core.entity.general.Cell
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.cellopt.vm.CellOptionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.cell_options_dialog.*
import javax.inject.Inject


@AndroidEntryPoint
class CellOptionsDialog: DialogFragment() {

    @Inject
    lateinit var vm: CellOptionsViewModel



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.also { AndroidUtil.removeDialogBackground(it) }
        return inflater.inflate(R.layout.cell_options_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields()
        val args = CellOptionsArguments.from(this)

        vm.requestData(args.jailId, args.cellNumber)
        vm.dataLD.observe(viewLifecycleOwner, Observer { data ->
            txtvCell.text = data
        })

        txtvUpdate.setOnClickListener {
            dismiss()
            vm.update()
        }
    }

//    override fun onDismiss(dialog: DialogInterface) {
//        super.onDismiss(dialog)
//        vm.notifyUiDismissed()
//    }


    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = CellOptionsViewModel.get(appContext, this)
    }


    companion object {
        fun arguments(cell: Cell): Bundle
            = CellOptionsArguments.create(cell)
    }
}