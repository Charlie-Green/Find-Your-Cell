package by.sviazen.prisoner.ui.cellopt.dialog

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.core.view.*
import by.sviazen.core.ui.common.SviazenDialog
import by.sviazen.core.util.android.DialogUtil
import by.sviazen.prisoner.R
import by.sviazen.prisoner.databinding.CellOptionsDialogBinding
import by.sviazen.prisoner.ui.cellopt.vm.CellOptionsViewModel
import by.sviazen.prisoner.ui.common.sched.period.ScheduleCellsCrudState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CellOptionsDialog: SviazenDialog<CellOptionsDialogBinding>() {

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

        vm.crudStateLD.observe(viewLifecycleOwner, { state ->
            renderState(state)
        })
    }


    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = CellOptionsViewModel.get(appContext, this)
    }


    private fun renderState(state: ScheduleCellsCrudState) {
        when(state) {
            is ScheduleCellsCrudState.ViewingOptions -> {
                val cell = state.target

                vb.txtvCell.text = "${cell.jailName}, ${cell.number}"
                vb.txtvAlert.visibility = View.GONE
                vb.txtvUpdateOrYes.setText(R.string.replace)
                vb.txtvDeleteOrNo.setText(R.string.delete)
                setBottomMargin(vb.txtvCell, R.dimen.celloptions_title_margin_vertical)

                vb.txtvUpdateOrYes.setOnClickListener {  // Update
                    dismiss()
                    vm.update()
                }
                vb.txtvDeleteOrNo.setOnClickListener {  // Delete
                    vm.delete()
                }
            }

            is ScheduleCellsCrudState.ConfirmingDelete -> {
                val cell = state.target

                vb.txtvCell.setText(R.string.delete_cell_alert_title)
                vb.txtvAlert.apply {
                    visibility = View.VISIBLE
                    text = getString(
                        R.string.delete_cell_alert_message,
                        cell.number,
                        cell.jailName
                    )
                }
                vb.txtvUpdateOrYes.setText(R.string.delete_yes)
                vb.txtvDeleteOrNo.setText(R.string.delete_no)

                vb.txtvUpdateOrYes.setOnClickListener {  // Yes
                    vm.confirmDelete()
                }
                vb.txtvDeleteOrNo.setOnClickListener {   // No
                    vm.declineDelete()
                }
            }

            else -> {
                dismiss()
            }
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
}