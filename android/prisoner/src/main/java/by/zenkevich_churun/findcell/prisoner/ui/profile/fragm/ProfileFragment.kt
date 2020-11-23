package by.zenkevich_churun.findcell.prisoner.ui.profile.fragm

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import by.zenkevich_churun.findcell.core.entity.Contact
import by.zenkevich_churun.findcell.core.entity.Prisoner
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.profile.vm.ProfileViewModel
import kotlinx.android.synthetic.main.profile_fragm.*


class ProfileFragment: Fragment(R.layout.profile_fragm) {

    private var prisonerId = Prisoner.INVALID_ID
    private var vm: ProfileViewModel? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prepareRecycler()
        val vm = getViewModel().also { this.vm = it }

        vm.prisonerLD.observe(viewLifecycleOwner, Observer { prisoner ->
            displayPrisoner(prisoner)
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        collectData()?.also { vm?.saveDraft(it) }
    }


    private fun prepareRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getViewModel(): ProfileViewModel {
        val appContext = requireContext().applicationContext
        return ProfileViewModel.get(appContext, this)
    }

    private fun displayPrisoner(prisoner: Prisoner) {
        prisonerId = prisoner.id
        val addedContactTypes = ProfileFragmentUtil.addedContactTypes(prisoner.contacts)

        tietName.setText(prisoner.name)
        recyclerView.adapter = ProfileRecyclerAdapter(prisoner, addedContactTypes)
    }

    private fun collectData(): Prisoner? {
        val adapter = recyclerView.adapter as ProfileRecyclerAdapter? ?: return null

        return PrisonerDraft(
            prisonerId,
            tietName.text.toString(),
            adapter.contacts,
            adapter.prisonerInfo
        ).also {
            Log.v("CharlieDebug", "${it.contacts.size} contacts, info=\"${it.info}\"")
        }
    }
}