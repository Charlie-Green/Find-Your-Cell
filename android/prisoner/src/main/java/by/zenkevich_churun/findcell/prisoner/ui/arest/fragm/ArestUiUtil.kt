package by.zenkevich_churun.findcell.prisoner.ui.arest.fragm

import by.zenkevich_churun.findcell.entity.Jail


internal object ArestUiUtil {

    fun jailsText(jails: Collection<Jail>): String {
        if(jails.isEmpty()) {
            return ""
        }

        val namesLen = jails.sumBy { jail ->
            jail.name.length
        }

        // Append ", " before each Jail except the first one:
        val textLen = namesLen + 2*(jails.size - 1)

        val sb = StringBuilder(textLen)
        for(jail in jails) {
            if(!sb.isEmpty()) {
                sb.append(", ")
            }

            sb.append(jail.name)
        }

        return sb.toString()
    }
}