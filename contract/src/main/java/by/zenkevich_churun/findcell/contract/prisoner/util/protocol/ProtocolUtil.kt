package by.zenkevich_churun.findcell.contract.prisoner.util.protocol

import java.util.Base64
import kotlin.jvm.Throws


object ProtocolUtil {

    @Throws(IllegalArgumentException::class)
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

            throw exc
        }
    }


    fun encodeBase64(data: ByteArray): String
        = Base64.getEncoder().encodeToString(data)
}