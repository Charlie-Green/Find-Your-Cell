package by.zenkevich_churun.findcell.domain.contract.jail

import by.zenkevich_churun.findcell.domain.entity.Cell
import com.google.gson.annotations.SerializedName


/** server -> client to get number of seats in each [Cell] of a jail. **/
class SeatCountsPojo(

    /** Element j is the value of [Cell.seats] for [Cell] #(j+1). **/
    @SerializedName("seats")
    var seats: List<Short>
)