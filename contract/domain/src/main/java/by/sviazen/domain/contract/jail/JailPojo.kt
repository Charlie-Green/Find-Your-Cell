package by.sviazen.domain.contract.jail

import by.sviazen.domain.entity.Jail
import com.google.gson.annotations.SerializedName


class JailPojo(
    @SerializedName("id")
    override var id: Int,

    @SerializedName("name")
    override val name: String,

    @SerializedName("cells")
    override val cellCount: Short
): Jail() {

    constructor(): this(Jail.UNKNOWN_ID, "", 0)


    companion object {

        fun from(
            jail: Jail
        ) = JailPojo(jail.id, jail.name, jail.cellCount)
    }
}