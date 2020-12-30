package by.zenkevich_churun.findcell.serial.prisoner.common

import by.zenkevich_churun.findcell.entity.entity.Prisoner
import by.zenkevich_churun.findcell.entity.response.LogInResponse
import by.zenkevich_churun.findcell.entity.response.SignUpResponse
import by.zenkevich_churun.findcell.serial.prisoner.v1.serial.PrisonerDeserializer1
import java.io.InputStream


interface PrisonerDeserializer {
    fun deserializeLogIn(input: InputStream): LogInResponse

    fun deserializeSignUp(
        input: InputStream,
        username: String,
        passwordHash: ByteArray,
        initialName: String
    ): SignUpResponse

    fun deserializePrisoner(input: InputStream): Prisoner


    companion object {
        fun forVersion(v: Int): PrisonerDeserializer = when(v) {
            1 -> PrisonerDeserializer1()
            else -> throw IllegalArgumentException("Unknown version $v")
        }
    }
}