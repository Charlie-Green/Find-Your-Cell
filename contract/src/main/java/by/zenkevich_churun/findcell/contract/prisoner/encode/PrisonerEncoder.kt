package by.zenkevich_churun.findcell.contract.prisoner.encode

import by.zenkevich_churun.findcell.contract.prisoner.entity.Prisoner


interface PrisonerEncoder {
    fun encode(prisoner: Prisoner): String


    companion object {
        fun forVersion(v: Int): PrisonerEncoder? = when(v) {
            1    -> PrisonerEncoder1()
            else -> null
        }
    }
}