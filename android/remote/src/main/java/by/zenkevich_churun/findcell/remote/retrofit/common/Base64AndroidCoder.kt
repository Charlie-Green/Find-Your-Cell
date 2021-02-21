package by.zenkevich_churun.findcell.remote.retrofit.common

import android.util.Base64
import by.zenkevich_churun.findcell.serial.common.abstr.Base64Coder
import by.zenkevich_churun.findcell.serial.common.v1.CommonContract1
import javax.inject.Inject


/** Implementation of [Base64Coder] for Android client. **/
class Base64AndroidCoder
@Inject constructor(): Base64Coder {

    override fun encode(data: ByteArray): String {
        val bytes = Base64.encode(data, Base64.URL_SAFE)
        return String(bytes, CommonContract1.ENCODING)
    }

    override fun decode(data: String): ByteArray {
        throw NotImplementedError("Client never decodes Base64")
    }
}