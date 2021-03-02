package by.zenkevich_churun.findcell.remote.retrofit.common

import by.zenkevich_churun.findcell.core.injected.common.Hasher
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton


/** Implementation of [Hasher] which uses SHA-512 algorithm **/
@Singleton  // To save time on initializing algorithm.
class Sha512Hasher @Inject constructor(): Hasher {

    private val digest by lazy {
        MessageDigest.getInstance("SHA-512")
    }


    override fun hash(data: String): ByteArray {
        val charset = Charsets.UTF_8
        return digest.digest( data.toByteArray(charset) )
    }
}