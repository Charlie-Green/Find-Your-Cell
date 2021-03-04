package by.sviazen.core.injected.common


/** Implementation of hash-coding. **/
interface Hasher {
    fun hash(data: String): ByteArray
}