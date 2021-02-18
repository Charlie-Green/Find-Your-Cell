package by.zenkevich_churun.findcell.serial.sync.v1

import by.zenkevich_churun.findcell.entity.entity.*
import by.zenkevich_churun.findcell.serial.prisoner.v1.pojo.ContactPojo1
import com.google.gson.annotations.SerializedName


class CoPrisonerPojo1: CoPrisoner() {

    @SerializedName("id")
    override var id: Int = Prisoner.INVALID_ID

    @SerializedName("name")
    override var name: String = ""

    @SerializedName("rel")
    var relationOrdinal: Short = - 1

    @SerializedName("jail")
    override var commonJailName: String = ""

    @SerializedName("cell")
    override var commonCellNumber: Short = 0


    override val relation: Relation
        get() = Relation.values()[relationOrdinal.toInt()]


    companion object {

        fun from(cp: CoPrisoner): CoPrisonerPojo1 {
            if(cp is CoPrisonerPojo1) {
                return cp
            }

            val pojo1 = CoPrisonerPojo1()
            pojo1.id = cp.id
            pojo1.name = cp.name
            pojo1.commonJailName = cp.commonJailName
            pojo1.commonCellNumber = cp.commonCellNumber
            pojo1.relationOrdinal = cp.relation.ordinal.toShort()

            return pojo1
        }
    }
}