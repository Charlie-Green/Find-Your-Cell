package by.zenkevich_churun.findcell.serial.jail.v1.pojo

import by.zenkevich_churun.findcell.entity.entity.Cell
import com.google.gson.annotations.SerializedName


class SeatCountsListPojo {

    /** Each element j in this list is the value of [Cell.seats] property
      * of [Cell] whose [Cell.number] is j+1. **/
    @SerializedName("seats")
    var seatCounts: ShortArray = shortArrayOf()


    companion object {

        fun wrap(cells: List<Cell>): SeatCountsListPojo {
            val pojo = SeatCountsListPojo()

            pojo.seatCounts = ShortArray(cells.size) { index ->
                cells[index].seats
            }

            return pojo
        }
    }
}