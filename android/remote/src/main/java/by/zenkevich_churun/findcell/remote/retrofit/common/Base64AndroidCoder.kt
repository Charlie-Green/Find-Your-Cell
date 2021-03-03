package by.zenkevich_churun.findcell.remote.retrofit.common

import android.util.Base64
import by.zenkevich_churun.findcell.domain.util.Base64Coder
import javax.inject.Inject


/** Implementation of [Base64Coder] for Android client. **/
class Base64AndroidCoder
@Inject constructor(): Base64Coder {

    override fun encode(data: ByteArray): String {
        val bytes = Base64.encode(data, Base64.URL_SAFE)
        return String(bytes, Charsets.UTF_8)
            .filter { char -> !char.isWhitespace() }
    }

    override fun decode(data: String): ByteArray {
        throw NotImplementedError("Client never decodes Base64")
    }
}