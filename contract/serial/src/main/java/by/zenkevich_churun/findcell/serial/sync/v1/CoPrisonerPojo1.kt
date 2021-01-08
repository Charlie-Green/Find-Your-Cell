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
    internal var contactPojos: List<ContactPojo1> = listOf()

    @SerializedName("rel")
    override var relationOrdinal: Int = - 1


    override val contacts: List<Contact>
        get() = contactPojos

    override val relation: Relation
        get() = Relation.values()[relationOrdinal]


    companion object {

        fun from(pojo: CoPrisonerPojo): CoPrisonerPojo1 {
            if(pojo is CoPrisonerPojo1) {
                return pojo
            }

            val pojo1 = CoPrisonerPojo1()
            pojo1.id = pojo.id
            pojo1.name = pojo.name
            pojo1.contactPojos = pojo.contacts.map { c ->
                ContactPojo1(c.type, c.data)
            }
            pojo1.relationOrdinal = pojo.relationOrdinal

            return pojo1
        }
    }
}