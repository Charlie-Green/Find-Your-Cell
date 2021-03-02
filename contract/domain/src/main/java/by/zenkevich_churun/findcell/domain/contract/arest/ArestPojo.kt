package by.zenkevich_churun.findcell.domain.contract.arest

import by.zenkevich_churun.findcell.domain.entity.Arest
import by.zenkevich_churun.findcell.domain.entity.LightArest
import com.google.gson.annotations.SerializedName


class ArestPojo(

    @SerializedName("id")
    override var id: Int,

    @SerializedName("jails")
    var jailIds: List<Int>,

    @SerializedName("start")
    override var start: Long,

    @SerializedName("end")
    override var end: Long

): LightArest() {


    constructor(): this(Arest.INVALID_ID, listOf(),0L, 0L)


    override val jailsCount: Int
        get() = jailIds.size

    override fun jailIdAt(index: Int): Int
        = jailIds[index]


    companion object {

        fun from(a: LightArest)
            = ArestPojo(a.id, jailIds(a), a.start, a.end)

        private fun jailIds(
            a: LightArest
        ) = List(a.jailsCount) { index ->
            a.jailIdAt(index)
        }
    }
}