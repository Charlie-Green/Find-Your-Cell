package by.zenkevich_churun.findcell.server.protocol.serial.common

import by.zenkevich_churun.findcell.serial.common.abstr.Base64Coder
import by.zenkevich_churun.findcell.serial.common.v1.CommonContract1
import java.util.Base64


class Base64ServerCoder: Base64Coder {

    override fun encode(data: ByteArray): String {
        throw NotImplementedError("Server never encodes Base64")
    }

    override fun decode(data: String): ByteArray {
        val bytes = data.toByteArray(CommonContract1.ENCODING)
        return Base64.getUrlDecoder().decode(bytes)
    }
}