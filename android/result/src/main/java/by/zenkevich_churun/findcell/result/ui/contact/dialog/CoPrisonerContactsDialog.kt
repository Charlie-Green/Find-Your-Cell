package by.zenkevich_churun.findcell.result.ui.contact.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import by.zenkevich_churun.findcell.core.ui.common.SviazenDialog
import by.zenkevich_churun.findcell.core.util.view.contact.ContactView
import by.zenkevich_churun.findcell.entity.entity.Prisoner
import by.zenkevich_churun.findcell.result.databinding.CoprisonerContactsDialogBinding
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

        vm.loadPrisoner(args.coprisonerId)
        vm.prisonerLD.observe(viewLifecycleOwner) { p ->
            displayData(p)
        }
    }


    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = CoPrisonerContactsViewModel.get(appContext, this)
    }


    private fun displayData(p: Prisoner) {
        // Remove previous ContactViews, if any:
        while(vb.root.childCount > 2) {
            vb.root.removeViewAt(1)
        }

        // Add the new ones:
        for(contact in p.contacts) {
            val view = ContactView(requireContext())
            view.show(contact)
            vb.root.addView(view)
        }

        // Update info:
        vb.txtvInfo.text = p.info
    }


    companion object {
        fun arguments(coPrisonerId: Int): Bundle
            = CoPrisonerContactsArguments.createBundle(coPrisonerId)
    }
}