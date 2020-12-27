package by.zenkevich_churun.findcell.contract.prisoner.v1.deserial

import by.zenkevich_churun.findcell.contract.entity.Prisoner
import by.zenkevich_churun.findcell.contract.prisoner.common.PrisonerDeserializer
import by.zenkevich_churun.findcell.contract.prisoner.v1.pojo.PrisonerPojo1
import com.google.gson.Gson
import java.io.InputStream
import java.io.InputStreamReader


internal class PrisonerDeserializer1: PrisonerDeserializer {

    override fun deserialize(input: InputStream): Prisoner {
        val reader = InputStreamReader(input)
        return Gson().fromJson(reader, PrisonerPojo1::class.java)
    }
}