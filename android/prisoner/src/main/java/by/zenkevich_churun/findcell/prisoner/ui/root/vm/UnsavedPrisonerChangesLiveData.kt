package by.zenkevich_churun.findcell.prisoner.ui.root.vm

import androidx.lifecycle.MediatorLiveData
import by.zenkevich_churun.findcell.prisoner.repo.profile.ProfileRepository
import by.zenkevich_churun.findcell.prisoner.ui.common.change.UnsavedChangesLiveDatasStorage


internal class UnsavedPrisonerChangesLiveData(
    store: UnsavedChangesLiveDatasStorage,
    profileRepo: ProfileRepository
): MediatorLiveData<Boolean>() {

    private var changesProfile = false
    private var changesSchedule = false


    init {
        value = false

        addSource(profileRepo.unsavedChangesLD) { changes ->
            changesProfile = changes
            updateValue()
        }

        addSource(store.scheduleLD) { changes ->
            changesSchedule = changes
            updateValue()
        }
    }


    private fun updateValue() {
        value = changesProfile || changesSchedule
    }
}