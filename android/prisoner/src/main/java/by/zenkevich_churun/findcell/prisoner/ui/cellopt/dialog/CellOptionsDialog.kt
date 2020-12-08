package by.zenkevich_churun.findcell.prisoner.ui.cellopt.dialog

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.core.view.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import by.zenkevich_churun.findcell.core.entity.general.Cell
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.cellopt.model.CellOptionsMode
import by.zenkevich_churun.findcell.prisoner.ui.cellopt.vm.CellOptionsViewModel
import by.zenkevich_churun.findcell.prisoner.ui.common.model.CellUpdate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.cell_options_dialog.*
import javax.inject.Inject


@AndroidEntryPoint
class CellOptionsDialog: DialogFragment() {

    @Inject
    lateinit var vm: CellOptionsViewModel

    private var mode = CellOptionsMode.OPTIONS
    private var cell: Cell? = null


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
        vm.dataLD.observe(viewLifecycleOwner, Observer { cell ->
            this.cell = cell
            updateTitle()
        })
        vm.modeLD.observe(viewLifecycleOwner, Observer { mode ->
            this.mode = mode
            enterMode()
        })
        vm.loadingLD.observe(viewLifecycleOwner, Observer { isLoading ->
            prBar.isVisible = isLoading
        })
        vm.cellUpdateLD.observe(viewLifecycleOwner, Observer { update ->
            handleUpdate(update)
        })
    }


    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = CellOptionsViewModel.get(appContext, this)
    }


    private fun enterMode() {
        when(mode) {
            CellOptionsMode.OPTIONS -> {
                txtvUpdateOrYes.setText(R.string.replace)
                txtvDeleteOrNo.setText(R.string.delete)

                txtvUpdateOrYes.setOnClickListener {  // Update
                    dismiss()
                    vm.update()
                }
                txtvDeleteOrNo.setOnClickListener {  // Delete
                    vm.delete()
                }
            }

            CellOptionsMode.CONFIRM_DELETE -> {
                txtvUpdateOrYes.setText(R.string.delete_yes)
                txtvDeleteOrNo.setText(R.string.delete_no)

                txtvUpdateOrYes.setOnClickListener {  // Yes
                    vm.confirmDelete()
                }
                txtvDeleteOrNo.setOnClickListener {   // No
                    vm.declineDelete()
                }
            }
        }

        updateTitle()
    }

    private fun updateTitle() {
        val cell = this.cell ?: return

        when(mode) {
            CellOptionsMode.OPTIONS -> {
                txtvCell.text = "${cell.jailName}, ${cell.number}"
                txtvAlert.visibility = View.GONE

                setBottomMargin(txtvCell, R.dimen.celloptions_title_margin_vertical)
            }

            CellOptionsMode.CONFIRM_DELETE -> {
                txtvCell.setText(R.string.delete_cell_alert_title)
                txtvAlert.apply {
                    visibility = View.VISIBLE
                    text = getString(
                        R.string.delete_cell_alert_message,
                        cell.number,
                        cell.jailName
                    )
                }

                setBottomMargin(txtvCell, 0)
                setBottomMargin(txtvAlert, R.dimen.celloptions_title_margin_vertical)
            }
        }
    }

    private fun handleUpdate(update: CellUpdate?) {
        if(update is CellUpdate.Deleted ||
            update is CellUpdate.DeleteFailed ) {

            dismiss()
        }
    }


    private fun setBottomMargin(view: View, marginRes: Int) {
        val margin =
            if(marginRes == 0) 0
            else resources.getDimensionPixelSize(marginRes)

        if(view.marginBottom == margin) {
            return
        }

        view.updateLayoutParams<LinearLayout.LayoutParams> {
            bottomMargin = margin
        }
    }


    companion object {
        fun arguments(cell: Cell): Bundle
            = CellOptionsArguments.create(cell)
    }
}