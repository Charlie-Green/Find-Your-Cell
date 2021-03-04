package by.sviazen.domain.util


/** Provides either Base64 encode (clients) or Base64 decode (server) operation. **/
interface Base64Coder {

    /** On client: encodes this byte array into a Base64 string.
      * It must use the encoding specified by the client-server contract.
      * On server: throws [NotImplementedError]. **/
    fun encode(data: ByteArray): String

    /** On client: throws [NotImplementedError].
      * On server: reverses the [encode] method. **/
    fun decode(data: String): ByteArray
}