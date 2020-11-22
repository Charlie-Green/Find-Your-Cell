package by.zenkevich_churun.findcell.prisoner.repo

import by.zenkevich_churun.findcell.core.api.LogInResponse
import by.zenkevich_churun.findcell.core.api.PrisonerApi
import by.zenkevich_churun.findcell.core.entity.Prisoner
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PrisonerRepository @Inject constructor(
    private val api: PrisonerApi ) {

    private var prisoner: Prisoner? = null


    fun logIn(username: String, password: String): LogInResponse {
        synchronized(this) {
            val prisoner = this.prisoner
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
                this.prisoner = response.prisoner
            }

            return response
        }
    }
}