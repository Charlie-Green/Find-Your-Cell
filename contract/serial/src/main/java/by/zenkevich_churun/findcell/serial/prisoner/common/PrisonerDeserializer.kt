package by.zenkevich_churun.findcell.serial.prisoner.common

import by.zenkevich_churun.findcell.entity.Prisoner
import by.zenkevich_churun.findcell.serial.prisoner.v1.deserial.PrisonerDeserializer1
import java.io.InputStream


interface PrisonerDeserializer {
    fun deserialize(input: InputStream): Prisoner


    companion object {
        fun forVersion(v: Int): PrisonerDeserializer = when(v) {
            1 -> PrisonerDeserializer1()
            else -> throw IllegalArgumentException("Unknown version $v")
        }
    }
}