package by.zenkevich_churun.findcell.result.ui.contact.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import by.zenkevich_churun.findcell.core.ui.common.SviazenDialog
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.core.util.view.contact.ContactView
import by.zenkevich_churun.findcell.entity.entity.Contact
import by.zenkevich_churun.findcell.entity.entity.Prisoner
import by.zenkevich_churun.findcell.result.R
import by.zenkevich_churun.findcell.result.databinding.CoprisonerContactsDialogBinding
import by.zenkevich_churun.findcell.result.ui.contact.model.GetCoPrisonerState
import by.zenkevich_churun.findcell.result.ui.contact.vm.CoPrisonerContactsViewModel


class CoPrisonerContactsDialog: SviazenDialog<CoprisonerContactsDialogBinding>() {

    private lateinit var vm: CoPrisonerContactsViewModel


    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = CoprisonerContactsDialogBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()

        val args = CoPrisonerContactsArguments(requireArguments())

        vm.loadPrisoner(args.coprisonerId, args.coprisonerName)
        vm.stateLD.observe(viewLifecycleOwner) { state ->
            displayData(state)
        }
    }


    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = CoPrisonerContactsViewModel.get(appContext, this)
    }


    private fun displayData(state: GetCoPrisonerState) {
        if(state is GetCoPrisonerState.Loading) {
            vb.prBar.visibility = View.VISIBLE
            return
        }
        if(state is GetCoPrisonerState.Error) {
            state.dialogConsumed = true
            dismiss()
            return
        }
        if(state !is GetCoPrisonerState.Success) {
            return
        }
        vb.prBar.visibility = View.GONE

        // Remove previous ContactViews, if any:
        while(vb.root.childCount > 2) {
            vb.root.removeViewAt(1)
        }

        // Add the new ones:
        for(contact in state.contacts) {
            addContactView(contact)
        }

        // Update name and info:
        vb.txtvName.text = state.name
        vb.txtvInfo.text = state.info
    }

    private fun addContactView(contact: Contact) {
        val gap = resources.getDimensionPixelSize(R.dimen.coprisoner_contacts_gap)

        val params = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            topMargin = gap
            bottomMargin = gap
        }

        val view = ContactView(requireContext())
        vb.root.addView(view, 1, params)
        view.show(contact)
    }


    companion object {

        fun arguments(
            coPrisonerId: Int,
            coPrisonerName: String

        ): Bundle = CoPrisonerContactsArguments.createBundle(
            coPrisonerId,
            coPrisonerName
        )
    }
}