package by.zenkevich_churun.findcell.serial.util.protocol

import kotlin.jvm.Throws


object ProtocolUtil {

    private const val JAVA_CLASS_NAME = "java.util.Base64"
    private const val ANDROID_CLASS_NAME = "android.util.Base64"


    @Throws(IllegalArgumentException::class)
    fun decodeBase64(
        base64: String,
        operation: String? = null
    ): ByteArray {

        try {
            val javaClass = Class.forName(JAVA_CLASS_NAME)
            return decodeBase64(javaClass, base64)

        } catch(exc: ClassNotFoundException) {
            val androidClass = Class.forName(ANDROID_CLASS_NAME)
            return decodeBase64(androidClass, base64)

        } catch(exc: IllegalArgumentException) {
            operation?.also {
                print("$it failed: ")
            }
            println("Can't decode Base64")

            throw exc
        }
    }


    fun encodeBase64(data: ByteArray): String {
        try {
            val javaClass = Class.forName(JAVA_CLASS_NAME)
            return encodeBase64(javaClass, data)
        } catch(exc: ClassNotFoundException) {
            val androidClass = Class.forName(ANDROID_CLASS_NAME)
            return encodeBase64(androidClass, data)
        }
    }


    private fun encodeBase64(
        base64Class: Class<out Any>,
        data: ByteArray
    ): String {

        if(base64Class.isJavaClass) {
            // Java class.
            // We have to obtain an instance via getEncoder() method
            val getEncoderMethod = base64Class.getMethod("getEncoder")
            val encoder = getEncoderMethod.invoke(null)
            val encodeMethod = encoder.javaClass.getMethod(
                "encodeToString", ByteArray::class.java )
            return encodeMethod.invoke(encoder, data) as String
        }

        // Android class.
        // encode and decode methods are invoked directly.

        val encodeMethod = base64Class
            .getMethod("encodeToString", ByteArray::class.java, Int::class.java)
        return encodeMethod.invoke(null, data, base64Class.androidDefaultFlag) as String
    }

    private fun decodeBase64(
        base64Class: Class<out Any>,
        data: String
    ): ByteArray {

        // Same logic as for encode:
        if(base64Class.isJavaClass) {
            val getDecoderMethod = base64Class.getMethod("getDecoder")
            val decoder = getDecoderMethod.invoke(null)
            val decodeMethod = decoder.javaClass.getMethod(
                "decode", String::class.java )
            return decodeMethod.invoke(decoder, data) as ByteArray
        }

        val decodeMethod = base64Class
            .getMethod("decode", Int::class.java)
        return decodeMethod
            .invoke(null, data, base64Class.androidDefaultFlag)
            as ByteArray
    }


    private val Class<out Any>.isJavaClass: Boolean
        get() = name.startsWith("java.")

    private val Class<out Any>.androidDefaultFlag: Int
        get() = getDeclaredField("DEFAULT").getInt(null)
}