package by.zenkevich_churun.findcell.core.injected.common


/** Implementation of hash-coding. **/
interface Hasher {
    fun hash(data: String): ByteArray
}