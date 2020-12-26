package by.zenkevich_churun.findcell.remote.lan.auth.pojo

import by.zenkevich_churun.findcell.core.entity.general.Contact
import by.zenkevich_churun.findcell.core.entity.general.Prisoner
import com.google.gson.annotations.SerializedName


internal class PrisonerPojo(

    @SerializedName("id")
    override val id: Int,

    @SerializedName("name")
    override val name: String,

    @SerializedName("info")
    override val info: String,

    @SerializedName("contacts")
    private val contactPojos: List<ContactPojo>
): Prisoner() {

    override val contacts: List<Contact> by lazy {
        contactPojos.map { pojo ->
            pojo.toContact()
        }
    }
}