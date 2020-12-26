package by.zenkevich_churun.findcell.contract.prisoner.decode

import by.zenkevich_churun.findcell.contract.prisoner.decode.v1.PrisonerDecoder1
import by.zenkevich_churun.findcell.contract.prisoner.entity.Prisoner
import java.io.InputStream


interface PrisonerDecoder {
    fun decode(istream: InputStream): Prisoner


    companion object {
        fun create(): PrisonerDecoder = PrisonerDecoder1()
    }
}