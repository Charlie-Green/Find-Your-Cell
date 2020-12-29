package by.zenkevich_churun.findcell.serial.jail.v1.pojo

import by.zenkevich_churun.findcell.entity.entity.Jail
import com.google.gson.annotations.SerializedName


class JailsListPojo1 {

    @SerializedName("jails")
    var jails: List<JailPojo1> = listOf()


    companion object {

        fun wrap(jails: List<Jail>): JailsListPojo1 {
            val pojo = JailsListPojo1()
            pojo.jails = jails.map { j ->
                JailPojo1.from(j)
            }

            return pojo
        }
    }
}