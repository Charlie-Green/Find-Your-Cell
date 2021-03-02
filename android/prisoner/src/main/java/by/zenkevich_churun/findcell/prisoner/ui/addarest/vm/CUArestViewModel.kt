package by.zenkevich_churun.findcell.prisoner.ui.addarest.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.domain.entity.Arest
import by.zenkevich_churun.findcell.domain.response.CreateOrUpdateArestResponse
import by.zenkevich_churun.findcell.prisoner.repo.arest.ArestsRepository
import by.zenkevich_churun.findcell.prisoner.ui.common.arest.ArestLiveDatasHolder
import by.zenkevich_churun.findcell.prisoner.ui.common.arest.ArestsListState
import by.zenkevich_churun.findcell.prisoner.ui.common.arest.CreateOrUpdateArestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class CUArestViewModel @Inject constructor(
    private val netTracker: NetworkStateTracker,
    private val holder: ArestLiveDatasHolder,
    private val repo: ArestsRepository
): ViewModel() {

    fun addArest(start: Long, end: Long) {
        val arests = this.arests ?: return

        if(!netTracker.isInternetAvailable) {
            holder.submitState( CreateOrUpdateArestState.NoInternet() )
            return
        }

        holder.submitState( CreateOrUpdateArestState.Loading )
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.addArest(start, end)
            applyResponse(arests, response.first, null, response.second)
        }
    }


    private val arests: List<Arest>?
        get() {
            val listState = holder.listStateLD.value as? ArestsListState.Loaded
            return listState?.arests
        }

    private fun applyResponse(
        arests: List<Arest>,
        response: CreateOrUpdateArestResponse,
        oldPosition: Int?,
        newPosition: Int ) {

        when(response) {
            is CreateOrUpdateArestResponse.NetworkError -> {
                val state = CreateOrUpdateArestState.NetworkError(oldPosition == null)
                holder.submitState(state)
            }

            is CreateOrUpdateArestResponse.ArestsIntersect -> {
                val intersectedArest = arests.find { a ->
                    a.id == response.intersectedId
                }
                val state = arestsIntersectState(intersectedArest, oldPosition == null)
                holder.submitState(state)
            }

            is CreateOrUpdateArestResponse.Success -> {
                val state = oldPosition?.let {
                    CreateOrUpdateArestState.Updated(it, newPosition)
                } ?: CreateOrUpdateArestState.Created(newPosition)
                holder.submitState(state)
            }
        }
    }

    private fun arestsIntersectState(
        intersectedArest: Arest?,
        operationCreate: Boolean
    ): CreateOrUpdateArestState {

        // This case is rare to impossible, yet it's implemented.
        // Ignore the add/update request.
        intersectedArest ?: return CreateOrUpdateArestState.Idle

        return CreateOrUpdateArestState.ArestsIntersectError(
            operationCreate,
            intersectedArest.start,
            intersectedArest.end
        )
    }


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): CUArestViewModel {

            val fact = CUArestVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(CUArestViewModel::class.java)
        }
    }
}