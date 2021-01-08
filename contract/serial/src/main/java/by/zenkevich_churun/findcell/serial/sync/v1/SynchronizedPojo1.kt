package by.zenkevich_churun.findcell.serial.sync.v1

import by.zenkevich_churun.findcell.entity.pojo.SynchronizedPojo
import com.google.gson.annotations.SerializedName


/** A large POJO containing all synced data. **/
class SynchronizedPojo1: SynchronizedPojo() {

    @SerializedName("jails")
    override var jails: List<FullJailPojo1> = listOf()

    @SerializedName("prisoners")
    override var coPrisoners: List<CoPrisonerPojo1> = listOf()


    companion object {

        fun from(pojo: SynchronizedPojo): SynchronizedPojo1 {
            if(pojo is SynchronizedPojo1) {
                return pojo
            }

            val pojo1 = SynchronizedPojo1()
            pojo1.jails = pojo.jails.map { j ->
                FullJailPojo1.from(j)
            }
            pojo1.coPrisoners = pojo.coPrisoners.map { p ->
                CoPrisonerPojo1.from(p)
            }

            return pojo1
        }
    }
}