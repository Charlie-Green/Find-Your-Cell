package by.zenkevich_churun.findcell.domain.contract.jail

import by.zenkevich_churun.findcell.domain.entity.Jail
import com.google.gson.annotations.SerializedName


class FullJailPojo(

    @SerializedName("id")
    override var id: Int,

    @SerializedName("name")
    override var name: String,

    @SerializedName("seats")
    var seatCounts: List<Short>

): Jail() {

    override val cellCount: Short
        get() = seatCounts.size.toShort()
}