package by.zenkevich_churun.findcell.entity.pojo

import by.zenkevich_churun.findcell.entity.entity.Cell
import by.zenkevich_churun.findcell.entity.entity.Jail


/** This POJO contains full information about a [Jail],
  * including information about all of its [Cell]s. **/
abstract class FullJailPojo: Jail() {

    /** Element j of this [List] is the value of [Cell.seats]
      * for the [Cell] #(j+1) within this [Jail].
      * A rare-to-impossible case: if such [Cell] is missing,
      * the corresponding position contains a negative value. **/
    abstract val cells: List<Short>


    override val cellCount: Short
        get() = cells.size.toShort()
}