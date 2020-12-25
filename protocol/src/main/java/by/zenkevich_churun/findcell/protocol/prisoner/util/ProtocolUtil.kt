package by.zenkevich_churun.findcell.server.protocol.util

import by.zenkevich_churun.findcell.server.protocol.exc.IllegalServerParameterException
import java.util.Base64
import kotlin.jvm.Throws


object ProtocolUtil {

    @Throws(IllegalServerParameterException::class)
    fun decodeBase64(
        base64: String,
        operation: String? = null
    ): ByteArray {

        try {
            return Base64.getDecoder().decode(base64)
        } catch(exc: IllegalArgumentException) {
            operation?.also {
                print("$it failed: ")
            }
            println("Can't decode Base64")

            throw IllegalServerParameterException()
        }
    }
}