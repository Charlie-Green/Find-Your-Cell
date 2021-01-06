package by.zenkevich_churun.findcell.prisoner.ui.cellopt.dialog

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.core.view.*
import androidx.lifecycle.Observer
import by.zenkevich_churun.findcell.core.ui.common.SviazenDialog
import by.zenkevich_churun.findcell.core.util.android.DialogUtil
import by.zenkevich_churun.findcell.entity.entity.Cell
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.databinding.CellOptionsDialogBinding
import by.zenkevich_churun.findcell.prisoner.ui.cellopt.model.CellOptionsMode
import by.zenkevich_churun.findcell.prisoner.ui.cellopt.vm.CellOptionsViewModel
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.ScheduleCellsCrudState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CellOptionsDialog: SviazenDialog<CellOptionsDialogBinding>() {

    private var mode = CellOptionsMode.OPTIONS
    private var cell: Cell? = null

    @Inject
    lateinit var vm: CellOptionsViewModel


    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = CellOptionsDialogBinding.inflate(inflater)

    override fun customizeDialog(view: View) {
        dialog?.also { DialogUtil.removeBackground(it) }
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
            vb.prBar.isVisible = isLoading
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
                vb.txtvUpdateOrYes.setText(R.string.replace)
                vb.txtvDeleteOrNo.setText(R.string.delete)

                vb.txtvUpdateOrYes.setOnClickListener {  // Update
                    dismiss()
                    vm.update()
                }
                vb.txtvDeleteOrNo.setOnClickListener {  // Delete
                    vm.delete()
                }
            }

            CellOptionsMode.CONFIRM_DELETE -> {
                vb.txtvUpdateOrYes.setText(R.string.delete_yes)
                vb.txtvDeleteOrNo.setText(R.string.delete_no)

                vb.txtvUpdateOrYes.setOnClickListener {  // Yes
                    vm.confirmDelete()
                }
                vb.txtvDeleteOrNo.setOnClickListener {   // No
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
                vb.txtvCell.text = "${cell.jailName}, ${cell.number}"
                vb.txtvAlert.visibility = View.GONE

                setBottomMargin(vb.txtvCell, R.dimen.celloptions_title_margin_vertical)
            }

            CellOptionsMode.CONFIRM_DELETE -> {
                vb.txtvCell.setText(R.string.delete_cell_alert_title)
                vb.txtvAlert.apply {
                    visibility = View.VISIBLE
                    text = getString(
                        R.string.delete_cell_alert_message,
                        cell.number,
                        cell.jailName
                    )
                }

                setBottomMargin(vb.txtvCell, 0)
                setBottomMargin(vb.txtvAlert, R.dimen.celloptions_title_margin_vertical)
            }
        }
    }

    private fun handleUpdate(update: ScheduleCellsCrudState?) {
        if(update is ScheduleCellsCrudState.Deleted ||
            update is ScheduleCellsCrudState.DeleteFailed ) {

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