package by.zenkevich_churun.findcell.prisoner.ui.arest.fragm

import android.annotation.SuppressLint
import by.zenkevich_churun.findcell.domain.entity.Jail
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("SimpleDateFormat")
internal object ArestUiUtil {

    private val dateFormat: DateFormat by lazy {
        SimpleDateFormat("dd.MM.yyyy")
    }


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


    fun format(time: Long): String {
        val date = Date(time)
        return dateFormat.format(date)
    }
}