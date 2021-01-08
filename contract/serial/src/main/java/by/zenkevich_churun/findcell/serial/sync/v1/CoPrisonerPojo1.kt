package by.zenkevich_churun.findcell.serial.sync.v1

import by.zenkevich_churun.findcell.entity.entity.Contact
import by.zenkevich_churun.findcell.entity.entity.Prisoner
import by.zenkevich_churun.findcell.serial.prisoner.v1.pojo.ContactPojo1
import by.zenkevich_churun.findcell.entity.pojo.CoPrisonerPojo
import com.google.gson.annotations.SerializedName


class CoPrisonerPojo1: CoPrisonerPojo() {

    @SerializedName("id")
    override var id: Int = Prisoner.INVALID_ID

    @SerializedName("name")
    override var name: String = ""

    @SerializedName("contacts")
    internal var contactPojos1: List<ContactPojo1> = listOf()

    @SerializedName("rel")
    override var relationOrdinal: Int = - 1


    override val contacts: List<Contact>
        get() = contactPojos1

    override val relation: Relation
        get() = Relation.values()[relationOrdinal]
}