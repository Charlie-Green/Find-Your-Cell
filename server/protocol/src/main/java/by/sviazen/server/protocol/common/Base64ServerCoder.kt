package by.sviazen.server.protocol.common

import by.sviazen.domain.util.Base64Coder
import java.util.Base64


class Base64ServerCoder: Base64Coder {

    override fun encode(data: ByteArray): String {
        throw NotImplementedError("Server never encodes Base64")
    }

    override fun decode(data: String): ByteArray {
        val bytes = data.toByteArray(Charsets.UTF_8)
        return Base64.getUrlDecoder().decode(bytes)
    }
}