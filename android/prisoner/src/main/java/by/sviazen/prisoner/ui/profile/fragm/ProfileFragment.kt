package by.sviazen.prisoner.ui.profile.fragm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import by.sviazen.core.ui.common.SviazenFragment
import by.sviazen.core.util.std.CollectionUtil
import by.sviazen.domain.entity.Contact
import by.sviazen.domain.entity.Prisoner
import by.sviazen.domain.simpleentity.SimplePrisoner
import by.sviazen.prisoner.R
import by.sviazen.prisoner.databinding.ProfileFragmBinding
import by.sviazen.prisoner.repo.profile.SavePrisonerResult
import by.sviazen.prisoner.ui.profile.vm.ProfileViewModel


/** Allows viewing and editing user's profile. **/
class ProfileFragment: SviazenFragment<ProfileFragmBinding>() {

    private var vm: ProfileViewModel? = null
    private var prisoner: Prisoner? = null
    private var addedContactTypes: MutableList<Contact.Type>? = null


    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = ProfileFragmBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prepareRecycler()
        setListeners()

        val vm = getViewModel().also { this.vm = it }
        vm.prisonerLD.observe(viewLifecycleOwner, { prisoner ->
            this.prisoner = prisoner
            displayPrisoner()
        })
        vm.addedContactTypesLD.observe(viewLifecycleOwner) { addedContactTypes ->
            this.addedContactTypes = CollectionUtil.copyList(addedContactTypes)
            displayPrisoner()
        }
        vm.unsavedChangesLD.observe(viewLifecycleOwner) { thereAreChanges ->
            vb.fabSave.isVisible = thereAreChanges
        }
        vm.loadingLD.observe(viewLifecycleOwner) { isLoading ->
            vb.prBar.isVisible = isLoading
        }
        vm.saveResultLD.observe(viewLifecycleOwner) { result ->
            when(result) {
                is SavePrisonerResult.Success    -> deleteContacts(result.deletedPositions)
                is SavePrisonerResult.Error      -> notifySaveError()
                is SavePrisonerResult.NoInternet -> notifySaveNeedsInternet()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveDraft()
    }


    private fun prepareRecycler() {
        vb.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setListeners() {
        vb.tietName.addTextChangedListener {
            if(vb.fabSave.visibility != View.VISIBLE &&
                prisoner != null &&
                prisoner?.name?.length != vb.tietName.text?.length ) {

                vm?.notifyDataChanged()
            }
        }

        vb.fabSave.setOnClickListener {
            collectData()?.also { vm?.save(it) }
        }
    }

    private fun getViewModel(): ProfileViewModel {
        val appContext = requireContext().applicationContext
        return ProfileViewModel.get(appContext, this)
    }


    private fun displayPrisoner() {
        val vm = this.vm ?: return
        val prisoner = this.prisoner ?: return
        val addedContactTypes = this.addedContactTypes ?: return

        vb.tietName.setText(prisoner.name)
        vb.recyclerView.adapter = ProfileRecyclerAdapter(vm, prisoner, addedContactTypes)
    }

    private fun collectData(): Prisoner? {
        val adapter = vb.recyclerView.adapter as ProfileRecyclerAdapter? ?: return null
        val prisoner = this.prisoner ?: return null

        return SimplePrisoner(
            prisoner.id,
            prisoner.username,
            prisoner.passwordHash ?: throw NullPointerException("Missing password hash"),
            vb.tietName.text.toString(),
            adapter.prisonerInfo,
            adapter.contacts
        )
    }

    private fun saveDraft() {
        val contactTypes = addedContactTypes ?: return
        val draft = collectData() ?: return
        vm?.saveDraft(draft, contactTypes)
    }

    private fun deleteContacts(positions: List<Int>) {
        if(positions.isEmpty()) {
            // Nothing to change.
            return
        }

        val adapter = vb.recyclerView.adapter as ProfileRecyclerAdapter? ?: return

        for(position in positions) {
            if(position !in adapter.contacts.indices) {
                continue
            }

            val contactType = adapter.contacts[position].type
            adapter.contacts.removeAt(position)
            adapter.notifyItemRemoved(position)
            addedContactTypes?.add(contactType)
        }

        // Modify addedContactTypes:
        adapter.notifyItemChanged(adapter.itemCount - 1)
    }


    private fun notifySaveError() = showErrorDialog(
        R.string.error_title,
        R.string.save_prisoner_error_msg
    ) {   }

    private fun notifySaveNeedsInternet() = showErrorDialog(
        R.string.no_internet_title,
        R.string.save_prisoner_needs_internet_msg
    ) {   }
}