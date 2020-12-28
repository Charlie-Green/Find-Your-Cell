package by.zenkevich_churun.findcell.serial.prisoner.v1.serial

import by.zenkevich_churun.findcell.entity.entity.Prisoner
import by.zenkevich_churun.findcell.entity.response.LogInResponse
import by.zenkevich_churun.findcell.entity.response.SignUpResponse
import by.zenkevich_churun.findcell.serial.common.v1.CommonContract1
import by.zenkevich_churun.findcell.serial.prisoner.common.PrisonerSerializer
import by.zenkevich_churun.findcell.serial.prisoner.v1.pojo.PrisonerPojo1
import com.google.gson.Gson
import com.google.gson.stream.JsonWriter
import java.io.ByteArrayOutputStream


/** Converts a Prisoner into JSON format. **/
internal class PrisonerSerializer1: PrisonerSerializer {

    override fun serialize(
        response: LogInResponse
    ): String = when(response) {
        is LogInResponse.WrongUsername -> "U"
        is LogInResponse.WrongPassword -> "P"
        is LogInResponse.Success -> serialize(response.prisoner)
        else -> throw IllegalArgumentException(response.javaClass.name)
    }

    override fun serialize(
        response: SignUpResponse
    ): String = when(response) {
        is SignUpResponse.UsernameTaken -> "U"
        is SignUpResponse.Success       -> "${response.prisoner.id}"
        else -> throw IllegalArgumentException(response.javaClass.name)
    }

    override fun serialize(prisoner: Prisoner): String {
        val ostream = ByteArrayOutputStream()
        val streamWriter = ostream.writer(CommonContract1.ENCODING)
        val jsonWriter = JsonWriter(streamWriter)
        val pojo = PrisonerPojo1.from(prisoner)

        Gson().toJson(pojo, pojo.javaClass, jsonWriter)
        jsonWriter.flush()

        return String(ostream.toByteArray(), CommonContract1.ENCODING)
    }
}