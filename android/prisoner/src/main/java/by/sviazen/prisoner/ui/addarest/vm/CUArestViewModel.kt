package by.sviazen.prisoner.ui.addarest.vm

import android.content.Context
import androidx.lifecycle.*
import by.sviazen.core.injected.web.NetworkStateTracker
import by.sviazen.core.repo.arest.AddOrUpdateArestResult
import by.sviazen.core.repo.arest.ArestsRepository
import by.sviazen.domain.entity.Arest
import by.sviazen.prisoner.ui.common.arest.ArestLiveDatasHolder
import by.sviazen.prisoner.ui.common.arest.ArestsListState
import by.sviazen.prisoner.ui.common.arest.CreateOrUpdateArestState
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
            holder.submitCU( CreateOrUpdateArestState.NoInternet() )
            return
        }

        holder.submitCU( CreateOrUpdateArestState.Loading )
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.addArest(start, end)
            applyResponse(arests, -1, result)
        }
    }


    private val arests: List<Arest>?
        get() {
            val listState = holder.listStateLD.value as? ArestsListState.Loaded
            return listState?.arests
        }

    private fun applyResponse(
        arests: List<Arest>,
        oldPosition: Int,
        result: AddOrUpdateArestResult ) {

        when(result) {
            is AddOrUpdateArestResult.NetworkError -> {
                val state = CreateOrUpdateArestState.NetworkError(oldPosition < 0)
                holder.submitCU(state)
            }

            is AddOrUpdateArestResult.ArestsIntersect -> {
                val intersectedArest = arests.find { a ->
                    a.id == result.intersectedId
                }
                val state = arestsIntersectState(intersectedArest, oldPosition < 0)
                holder.submitCU(state)
            }

            is AddOrUpdateArestResult.Success -> {
                val state = if(oldPosition < 0) {
                    CreateOrUpdateArestState.Created(result.arestPosition)
                } else {
                    CreateOrUpdateArestState.Updated(oldPosition, result.arestPosition)
                }

                holder.submitCU(state)
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