package by.zenkevich_churun.findcell.serial.prisoner.v1.deserial

import by.zenkevich_churun.findcell.entity.entity.Prisoner
import by.zenkevich_churun.findcell.entity.response.LogInResponse
import by.zenkevich_churun.findcell.entity.response.SignUpResponse
import by.zenkevich_churun.findcell.serial.common.v1.CommonContract1
import by.zenkevich_churun.findcell.serial.prisoner.common.PrisonerDeserializer
import by.zenkevich_churun.findcell.serial.prisoner.common.SignedUpPrisoner
import by.zenkevich_churun.findcell.serial.prisoner.v1.pojo.PrisonerPojo1
import com.google.gson.Gson
import java.io.InputStream


internal class PrisonerDeserializer1: PrisonerDeserializer {

    override fun deserializeLogIn(
        input: InputStream
    ): LogInResponse {

        val bytes = input.readBytes()
        val data = String(bytes, CommonContract1.ENCODING)
        println("Deserializing:")
        for(b in bytes) {
            println("  ${b.toString(16)}")
        }

        if(data == "U") {
            return LogInResponse.WrongUsername
        }
        if(data == "P") {
            return LogInResponse.WrongPassword
        }

        val prisoner = deserializePrisoner(data)
        return LogInResponse.Success(prisoner)
    }

    override fun deserializeSignUp(
        input: InputStream,
        username: String,
        passwordHash: ByteArray,
        initialName: String
    ): SignUpResponse {

        val data = String(input.readBytes(), CommonContract1.ENCODING)

        if(data == "U") {
            return SignUpResponse.UsernameTaken
        }

        val prisoner = SignedUpPrisoner(
            data.toInt(),
            username,
            passwordHash,
            initialName
        )
        return SignUpResponse.Success(prisoner)
    }

    override fun deserializePrisoner(input: InputStream): Prisoner {
        val data = String(input.readBytes(), CommonContract1.ENCODING)
        return deserializePrisoner(data)
    }


    private fun deserializePrisoner(data: String): Prisoner {
        return Gson().fromJson(data, PrisonerPojo1::class.java)
    }
}