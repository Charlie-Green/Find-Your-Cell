package by.zenkevich_churun.findcell.prisoner.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.zenkevich_churun.findcell.core.api.LogInResponse
import by.zenkevich_churun.findcell.core.api.PrisonerApi
import by.zenkevich_churun.findcell.core.entity.Contact
import by.zenkevich_churun.findcell.core.entity.Prisoner
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PrisonerRepository @Inject constructor(
    private val api: PrisonerApi ) {

    private val mldPrisoner = MutableLiveData<Prisoner>().apply {
        value = object: Prisoner() {
            override val id: Int
                get() = 17

            override val name: String
                get() = "Simon"

            override val contacts: List<Contact>
                get() = listOf(
                    Contact.Phone("+1 456 1234567"),
                    Contact.Skype("live:simon_fake")
                )

            override val info: String
                get() = "Fake User"
        }
    }

    val prisoneeLD: LiveData<Prisoner>
        get() = mldPrisoner


    fun logIn(username: String, password: String): LogInResponse {
        val prisoner = mldPrisoner.value
        if(prisoner != null) {
            return LogInResponse.Success(prisoner)
        }

        val passHash = password.toByteArray(Charsets.UTF_16)
        val response = try {
            api.logIn(username, passHash)
        } catch(exc: IOException) {
            LogInResponse.Error(exc)
        }

        if(response is LogInResponse.Success) {
            mldPrisoner.postValue(response.prisoner)
        }

        return response
    }
}