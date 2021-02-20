package by.zenkevich_churun.findcell.prisoner.repo.profile

import android.content.Context
import by.zenkevich_churun.findcell.entity.entity.Prisoner
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException


/** Stores some data that:
  * - is related to authorization;
  * - is NOT user-sensitive data;
  * - required to perform authorization operations correctly. **/
internal class AuthorizationMetadataStorage(
    private val appContext: Context) {

    var lastPrisonerId: Int
        get() {
            return readFile(Prisoner.INVALID_ID) { dis ->
                dis.readInt()
            }
        }
        set(value) {
            writeFile { dos ->
                dos.writeInt(value)
            }
        }


    private inline fun <T> readFile(
        default: T,
        extractData: (DataInputStream) -> T
    ): T {
        try {
            return appContext.openFileInput(FILENAME).use { fis ->
                DataInputStream(fis).use { dis ->
                    extractData(dis)
                }
            }
        } catch(exc: IOException) {
            return default
        }
    }

    private inline fun writeFile(
        writeData: (DataOutputStream) -> Unit ) {

        appContext.openFileOutput(FILENAME, Context.MODE_PRIVATE).use { fos ->
            DataOutputStream(fos).use { dos ->
                writeData(dos)
            }
        }
    }


    companion object {
        private const val FILENAME = "auth-meta.bin"
    }
}