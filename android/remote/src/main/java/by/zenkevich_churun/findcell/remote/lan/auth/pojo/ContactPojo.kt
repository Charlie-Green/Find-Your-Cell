package by.zenkevich_churun.findcell.remote.lan.auth.pojo

import by.zenkevich_churun.findcell.core.entity.general.Contact
import com.google.gson.annotations.SerializedName


class ContactPojo(

    @SerializedName("type")
    val type: Short,

    @SerializedName("data")
    val data: String ) {


    fun toContact(): Contact {
        TODO()
    }
}