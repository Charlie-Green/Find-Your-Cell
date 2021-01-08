package by.zenkevich_churun.findcell.serial.sync.v1

import by.zenkevich_churun.findcell.entity.entity.Cell
import by.zenkevich_churun.findcell.entity.entity.Jail
import by.zenkevich_churun.findcell.entity.pojo.FullJailPojo
import com.google.gson.annotations.SerializedName


/** This POJO contains full information about a [Jail],
  * including information about all of its [Cell]s. **/
class FullJailPojo1: FullJailPojo() {

    @SerializedName("id")
    override var id: Int = Jail.UNKNOWN_ID

    @SerializedName("name")
    override var name: String = ""

    /** Element j of this [List] is the value of [Cell.seats]
      * for the [Cell] #(j+1) within this [Jail].
      * A rare-to-impossible case: if such [Cell] is missing,
      * the corresponding position contains a negative value. **/
    @SerializedName("cells")
    override var cells: List<Short> = listOf()


    override val cellCount: Short
        get() = cells.size.toShort()


    companion object {

        fun from(pojo: FullJailPojo): FullJailPojo1 {
            if(pojo is FullJailPojo1) {
                return pojo
            }

            val pojo1 = FullJailPojo1()
            pojo1.id    = pojo.id
            pojo1.name  = pojo.name
            pojo1.cells = pojo.cells

            return pojo1
        }
    }
}