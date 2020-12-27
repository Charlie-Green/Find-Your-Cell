package by.zenkevich_churun.findcell.contract.prisoner.common

import by.zenkevich_churun.findcell.contract.entity.Prisoner
import by.zenkevich_churun.findcell.contract.prisoner.v1.serial.PrisonerSerializer1


interface PrisonerSerializer {
    fun serialize(prisoner: Prisoner): String


    companion object {
        fun forVersion(v: Int): PrisonerSerializer = when(v) {
            1 -> PrisonerSerializer1()
            else -> throw IllegalArgumentException("Unknown version $v")
        }
    }
}