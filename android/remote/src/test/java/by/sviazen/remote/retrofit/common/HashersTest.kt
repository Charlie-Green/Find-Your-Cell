package by.sviazen.remote.retrofit.common

import by.sviazen.core.injected.common.Hasher
import by.sviazen.serial.common.v1.CommonContract1
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class HashersTest {

    private val sha512 = Sha512Hasher()


    @Test
    fun sha512_111() = test(
        sha512,
        "111",
        "fb131bc57a477c8c",
        "de72b85822e39fdd"
    )

    @Test
    fun sha512_222() = test(
        sha512,
        "222",
        "5f28f24f5520230f",
        "9252261778d406eb"
    )

    @Test
    fun sha512_333() = test(
        sha512,
        "333",
        "5e3155774d39d97c",
        "8e85f91bd41e7eeb"
    )

    @Test
    fun sha512_444() = test(
        sha512,
        "444",
        "a5e4209e841321ae",
        "b6ec3b6f18150c4b"
    )


    private fun test(
        hasher: Hasher,
        input: String,
        expectedBeginningHex: String,
        expectedEndingHex: String ) {

        val actual = hasher.hash(input)
        assertRange(actual,             0,           8, expectedBeginningHex)
        assertRange(actual, actual.size-8, actual.size, expectedEndingHex)
    }

    private fun assertRange(
        actual: ByteArray,
        startIndex: Int,
        endIndex: Int,
        expectedHex: String ) {

        val expectedRange = parseHex(expectedHex)
        val actualRange = actual.copyOfRange(startIndex, endIndex)
        Assertions.assertArrayEquals(expectedRange, actualRange)
    }

    private fun bytesString(bytes: ByteArray): String {
        val sb = StringBuilder(3*bytes.size)
        for(byte in bytes) {
            val byteStr = byte.toUByte().toString(16).padStart(2, '0')
            sb.append(" $byteStr")
        }

        return sb.toString()
    }

    private fun parseHex(str: String): ByteArray {
        return ByteArray(str.length / 2) { index ->
            val substr = str.substring(2*index, 2*index+2)
            substr.toUByte(16).toByte()
        }
    }
}