package by.zenkevich_churun.findcell.prisoner.ui.cell.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.entity.general.Jail
import by.zenkevich_churun.findcell.core.util.std.max
import by.zenkevich_churun.findcell.prisoner.repo.jail.GetJailsResult
import by.zenkevich_churun.findcell.prisoner.repo.jail.JailsRepository
import by.zenkevich_churun.findcell.prisoner.ui.cell.model.CellEditorState
import by.zenkevich_churun.findcell.prisoner.ui.cell.model.JailHeader
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class CellViewModel @Inject constructor(
    @ApplicationContext appContext: Context,
    private val repo: JailsRepository
): ViewModel() {

    private val mapping = CellVMMapping(appContext)
    private val mldEditorState = MutableLiveData<CellEditorState>()
    private val mldLoading = MutableLiveData<Boolean>()
    private val mldError = MutableLiveData<String?>()


    val editorStateLD: LiveData<CellEditorState>
        get() = mldEditorState

    val loadingLD: LiveData<Boolean>
        get() = mldLoading

    val errorLD: LiveData<String?>
        get() = mldError


    fun requestState(jailId: Int, cellNumber: Short) {
        if(mldEditorState.value != null) {
            return
        }

        if(getAndSetLoading()) {
            return
        }
        mldError.value = null

        viewModelScope.launch(Dispatchers.IO) {
            // TODO: Provide the real value of 'internet' parameter.
            val result = repo.jailsList(true)

            when(result) {
                is GetJailsResult.Success -> {
                    val state = createState(result.jails, jailId, cellNumber)
                    mldEditorState.postValue(state)
                }

                is GetJailsResult.FirstTimeNeedInternet -> {
                    mldError.postValue(mapping.needInternetMessage)
                }

                is GetJailsResult.FirstTimeError -> {
                    mldError.postValue(mapping.errorMessage)
                }
            }

            mldLoading.postValue(false)
        }
    }


    private fun getAndSetLoading(): Boolean {
        val isLoading = mldLoading.value ?: false
        mldLoading.value = true
        return isLoading
    }

    private fun createState(
        jails: List<Jail>,
        jailId: Int,
        cellNumber: Short
    ): CellEditorState {

        val jailHeaders = jails.map { jail ->
            JailHeader.from(jail)
        }

        val jailIndex = jails.indexOfFirst { jail ->
            jail.id == jailId
        }

        return CellEditorState(
            jailHeaders,
            if(jailIndex in jails.indices) jailIndex else 0,
            max(cellNumber, 1.toShort()),
            true
        )
    }


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): CellViewModel {

            val fact = CellVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(CellViewModel::class.java)
        }
    }
}