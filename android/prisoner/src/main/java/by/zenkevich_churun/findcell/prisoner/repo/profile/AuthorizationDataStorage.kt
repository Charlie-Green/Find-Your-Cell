package by.zenkevich_churun.findcell.prisoner.repo.profile

import android.content.Context
import by.zenkevich_churun.findcell.entity.entity.Prisoner
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException


/** Stores some data related to authorization ([Prisoner] ID, as of version 1.0)
  * required for some functionality (sync, as of version 1.0) to work correctly. **/
internal class AuthorizationDataStorage(private val appContext: Context) {

    var lastPrisonerId: Int
        get() {
            return try {
                appContext.openFileInput(FILENAME).use { istream ->
                    DataInputStream(istream).use { dis ->
                        dis.readInt()
                    }
                }
            } catch(exc: IOException) {
                return Prisoner.INVALID_ID
            }
        }
        set(value) {
            appContext.openFileOutput(FILENAME, Context.MODE_PRIVATE).use { ostream ->
                DataOutputStream(ostream).use { dos ->
                    dos.writeInt(value)
                }
            }
        }


    companion object {
        private const val FILENAME = "auth.bin"
    }
}