package by.zenkevich_churun.findcell.prisoner.ui.profile.vm

import androidx.lifecycle.*


class ProfileViewModel: ViewModel() {
    

    companion object {
        fun get(storeOwner: ViewModelStoreOwner): ProfileViewModel {
            val provider = ViewModelProvider(storeOwner, ProfileVMFactory)
            return provider.get(ProfileViewModel::class.java)
        }
    }
}