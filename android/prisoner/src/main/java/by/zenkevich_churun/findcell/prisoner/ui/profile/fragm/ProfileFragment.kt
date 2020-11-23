package by.zenkevich_churun.findcell.prisoner.ui.profile.fragm

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import by.zenkevich_churun.findcell.core.entity.Contact
import by.zenkevich_churun.findcell.core.entity.Prisoner
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.profile.vm.ProfileViewModel
import kotlinx.android.synthetic.main.profile_fragm.*


class ProfileFragment: Fragment(R.layout.profile_fragm) {

    private var prisoner: Prisoner? = null
    private var vm: ProfileViewModel? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prepareRecycler()
        setListeners()

        val vm = getViewModel().also { this.vm = it }
        vm.prisonerLD.observe(viewLifecycleOwner, Observer { prisoner ->
            displayPrisoner(prisoner)
        })
        vm.unsavedChangesLD.observe(viewLifecycleOwner, Observer { thereAreChanges ->
            if(thereAreChanges) {
                fabSave.visibility = View.VISIBLE
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if(fabSave.visibility == View.VISIBLE) {
            // There are unsaved changes.
            // Flush them down the app architecture to survive configuration change:
            collectData()?.also {
                vm?.saveDraft(it)
            }
        }
    }


    private fun prepareRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setListeners() {
        tietName.addTextChangedListener {
            if(fabSave.visibility != View.VISIBLE &&
                prisoner != null &&
                prisoner?.name?.length != tietName.text?.length ) {

                fabSave.visibility = View.VISIBLE
            }
        }
    }

    private fun getViewModel(): ProfileViewModel {
        val appContext = requireContext().applicationContext
        return ProfileViewModel.get(appContext, this)
    }

    private fun displayPrisoner(prisoner: Prisoner) {
        this.prisoner = prisoner
        val addedContactTypes = ProfileFragmentUtil.addedContactTypes(prisoner.contacts)

        tietName.setText(prisoner.name)
        recyclerView.adapter = ProfileRecyclerAdapter(prisoner, addedContactTypes) {
            /* Data Updated: */
            Log.v("CharlieDebug", "Info changed")
            fabSave.visibility = View.VISIBLE
        }
    }

    private fun collectData(): Prisoner? {
        val adapter = recyclerView.adapter as ProfileRecyclerAdapter? ?: return null
        val prisoner = this.prisoner ?: return null

        return PrisonerDraft(
            prisoner.id,
            tietName.text.toString(),
            adapter.contacts,
            adapter.prisonerInfo
        ).also {
            Log.v("CharlieDebug", "${it.contacts.size} contacts, info=\"${it.info}\"")
        }
    }
}