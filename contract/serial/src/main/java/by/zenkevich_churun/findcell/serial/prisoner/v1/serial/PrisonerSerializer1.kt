package by.zenkevich_churun.findcell.serial.prisoner.v1.serial

import by.zenkevich_churun.findcell.entity.entity.Prisoner
import by.zenkevich_churun.findcell.entity.response.LogInResponse
import by.zenkevich_churun.findcell.entity.response.SignUpResponse
import by.zenkevich_churun.findcell.serial.common.abstr.Base64Coder
import by.zenkevich_churun.findcell.serial.prisoner.common.PrisonerSerializer
import by.zenkevich_churun.findcell.serial.prisoner.v1.pojo.PrisonerPojo1
import by.zenkevich_churun.findcell.serial.util.protocol.ProtocolUtil


/** Converts a Prisoner into JSON format. **/
internal class PrisonerSerializer1(
    private val base64: Base64Coder
): PrisonerSerializer {

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
        val passBase64 = prisoner.passwordHash?.let { base64.encode(it) }
        val pojo = PrisonerPojo1.from(prisoner, passBase64)
        return ProtocolUtil.toJson(pojo, 192)
    }
}