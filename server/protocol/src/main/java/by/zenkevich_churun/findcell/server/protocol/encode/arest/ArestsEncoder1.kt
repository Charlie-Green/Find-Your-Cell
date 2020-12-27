package by.zenkevich_churun.findcell.server.protocol.encode.arest

import by.zenkevich_churun.findcell.contract.arest.v1.ArestContract1
import by.zenkevich_churun.findcell.server.internal.entity.view.ArestView


internal class ArestsEncoder1: ArestsEncoder {

    override fun encode(arests: List<ArestView>): String {
        // Approximation: About 74 chars per Arest, + 3 chars for the outer '{}'
        val sb = StringBuilder(3 + 74*arests.size)
        sb.append("{")

        for(arest in arests) {
            if(sb.length > 2) {
                sb.append(",")
            }

            appendArest(sb, arest)
        }

        sb.append("\n}")

        return sb.toString()
    }


    private fun appendArest(sb: StringBuilder, arestView: ArestView) {
        sb.append(
            "\n{" +
                   "\"${ArestContract1.KEY_AREST_ID}\": ${arestView.arest.id}\n" +
                   "\"${ArestContract1.KEY_AREST_START}\": ${arestView.arest.start}\n" +
                   "\"${ArestContract1.KEY_AREST_END}\": ${arestView.arest.end}\n" +
                   "\"${ArestContract1.KEY_JAIL_IDS}\": ["
        )

        for(jailId in arestView.jailIds) {
            if(!sb.endsWith('[')) {
                sb.append(",")
            }

            sb.append(" $jailId")
        }
        sb.append(" ]}")
    }
}