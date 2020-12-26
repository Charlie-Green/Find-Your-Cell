package by.zenkevich_churun.findcell.contract.prisoner.decode.v1

import by.zenkevich_churun.findcell.contract.prisoner.decode.PrisonerDecoder
import by.zenkevich_churun.findcell.contract.prisoner.entity.Prisoner
import com.google.gson.Gson
import java.io.InputStream
import java.io.InputStreamReader


internal class PrisonerDecoder1: PrisonerDecoder {

    override fun decode(istream: InputStream): Prisoner {
        val reader = InputStreamReader(istream)
        val prisonerPojo = Gson().fromJson(reader, PrisonerPojo1::class.java)
        return prisonerPojo.toPrisoner()
    }
}