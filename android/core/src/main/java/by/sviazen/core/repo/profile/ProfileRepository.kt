package by.sviazen.core.repo.profile

import androidx.lifecycle.LiveData
import by.sviazen.domain.contract.auth.LogInResponse
import by.sviazen.domain.contract.auth.SignUpResponse
import by.sviazen.domain.entity.Prisoner


interface ProfileRepository {

    val prisonerLD: LiveData<out Prisoner?>

    val unsavedChangesLD: LiveData<Boolean>

    val savePrisonerResultLD: LiveData<SavePrisonerResult>

    fun logIn(username: String, password: String): LogInResponse

    fun signUp(
        username: String,
        password: String
    ): SignUpResponse

    fun logOut()

    fun saveDraft(draft: Prisoner)

    fun save(data: Prisoner, internet: Boolean)

    fun notifySaveResultConsumed()

    fun notifyDataChanged()

    fun withdrawUnsavedChanges()
}