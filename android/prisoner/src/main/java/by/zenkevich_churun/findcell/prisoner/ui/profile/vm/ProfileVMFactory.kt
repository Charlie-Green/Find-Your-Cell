package by.zenkevich_churun.findcell.prisoner.ui.profile.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


internal object ProfileVMFactory: ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel() as T
    }
}